package com.linkedin.pid.simulator.util;

import java.util.concurrent.FutureTask;

/**
 * Created by scho on 12/16/16.
 */

public class TaskAccessibleFutureTask<V> extends FutureTask {
    private Runnable task;

    public TaskAccessibleFutureTask(Runnable runnable, V result) {
        super(runnable, result);
        this.task = runnable;
    }

    public Runnable getTask() {
        return task;
    }
}
