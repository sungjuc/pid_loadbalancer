package com.linkedin.pid.simulator;

import com.linkedin.pid.simulator.util.TaskAccessibleFutureTask;
import com.linkedin.pid.simulator.util.TaskAccessibleThreadPoolExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by scho on 10/21/16.
 */
public class MultiThreadedServer implements Server {
    ExecutorService executorService;
    private int serverId;
    private BlockingQueue<Runnable> queue;
    private AtomicBoolean running;
    private AtomicInteger count;
    private AtomicInteger pendingRequests;
    private AtomicLong pendingCosts;

    private int corePoolSize = 3;
    private int maxPoolSize = 5;
    private int keepAliveTime = 1;

    private RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            pendingRequests.decrementAndGet();
            TaskAccessibleFutureTask futureTask = (TaskAccessibleFutureTask) r;
            Request request = (Request) futureTask.getTask();
            pendingCosts.addAndGet(-1 * request.getCost());
            request.error();
            //System.out.println("Rejected!!");
        }
    };

    public MultiThreadedServer(int serverId) {
        this.serverId = serverId;
        queue = new LinkedBlockingDeque<Runnable>(100);
        running = new AtomicBoolean(true);
        count = new AtomicInteger(0);
        pendingRequests = new AtomicInteger(0);
        pendingCosts = new AtomicLong(0);

        executorService = new TaskAccessibleThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue, rejectedExecutionHandler);
    }


    public static List<Server> CreateServerList(int numServers) throws InterruptedException {
        List<Server> serverList = new ArrayList<Server>(numServers);
        for (int i = 0; i < numServers; i++) {
            MultiThreadedServer server = new MultiThreadedServer(i);
            serverList.add(server);
            server.start();
        }

        return serverList;
    }

    public void start() {

    }

    public boolean process(Request request) {
        // System.out.println("request process start!" + queue.size());
        pendingRequests.incrementAndGet();
        pendingCosts.addAndGet(request.getCost());
        request.setServer(this);
        executorService.submit(request);
        request.setQueueTime(System.currentTimeMillis());
        return true;
    }

    public void shutdown() {
        running.compareAndSet(true, false);
        executorService.shutdown();
    }

    @Override
    public int getId() {
        return serverId;
    }

    @Override
    public void done(Request request) {
        pendingRequests.decrementAndGet();
        pendingCosts.addAndGet(-1 * request.getCost());
    }

    @Override
    public long getPendingCosts() {
        return pendingCosts.get();
    }

    @Override
    public int getPendingRequests() {
        return pendingRequests.get();
    }
}
