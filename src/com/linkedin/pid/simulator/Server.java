package com.linkedin.pid.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scho on 10/10/16.
 */
public interface Server{
    public boolean process (Request request);

    public void shutdown();

    public int getId();

    void done(Request request);

    long getPendingCosts();

    int getPendingRequests();
}
