package com.linkedin.pid.simulator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scho on 10/10/16.
 */
public class Request implements Runnable {
    private Client caller;
    private int requestId;
    private long creationTime;
    private long sendTime;
    private long queueTime;
    private long processTime;
    private long finishTime;
    private long returnTime;
    private long cost;
    private Server server;
    private Stats stats;
    private boolean success;

    public Request(int requestId, long cost, Stats stats) {
        this.requestId = requestId;
        this.cost = cost;
        creationTime = System.currentTimeMillis();
        queueTime = 0;
        this.stats = stats;
    }

    public void send(Client client, Server server) {
        sendTime = System.currentTimeMillis();
        caller = client;
        server.process(this);
    }

    public void setQueueTime(long queueTime) {
        this.queueTime = queueTime;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public long getCost() {
        return this.cost;
    }

    public void wrap() {
        this.success = true;
        returnTime = System.currentTimeMillis();
        long totalLatency = returnTime - creationTime;
        long totalQueueTime = processTime - queueTime;
        long totalProcessTime = finishTime - processTime;
        stats.update(totalLatency, totalQueueTime, totalProcessTime, success);
    }

    public void error() {
        this.success = false;
        returnTime = System.currentTimeMillis();
        long totalLatency = returnTime - creationTime;
        stats.update(totalLatency, -1, -1, success);
        caller.handleError(this);
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String toString() {
        if (success) {
            return "requestId = " + requestId + ", serverId = " + server.getId() + ", cost = " + cost +
                    ", total latency = " + (returnTime - creationTime) + ", queueWaitTime = " + (processTime - queueTime) +
                    ", processTime = " + (finishTime - processTime);
        } else {
            return "requestId = " + requestId + ", serverId = " + server.getId() + ", cost = " + cost +
                    ", total latency = " + (returnTime - creationTime) + ", error";
        }
    }

    @Override
    public void run() {
        //System.out.println("request run !!!");
        processTime = System.currentTimeMillis();
        try {
            Thread.sleep(cost);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finishTime = System.currentTimeMillis();
        server.done(this);
        caller.handleResponse(this);
        //System.out.println("request done !!!");
    }
}
