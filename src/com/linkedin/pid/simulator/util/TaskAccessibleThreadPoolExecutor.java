package com.linkedin.pid.simulator.util;

import java.util.concurrent.*;

/**
 * Created by scho on 12/16/16.
 */
public class TaskAccessibleThreadPoolExecutor extends ThreadPoolExecutor {
    public TaskAccessibleThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler rejectedExecutionHandler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, rejectedExecutionHandler);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new TaskAccessibleFutureTask<T>(runnable, value);
    }
}
