package com.linkedin.pid.simulator;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by scho on 10/21/16.
 */
public class Stats {
    private AtomicLong totalLatency;
    private AtomicLong totalQueueTime;
    private AtomicLong totalProcessTime;
    private AtomicInteger resultSize;

    public Stats() {
        init();
    }

    public void init() {
        totalLatency = new AtomicLong(0);
        totalQueueTime = new AtomicLong(0);
        totalProcessTime = new AtomicLong(0);
        resultSize = new AtomicInteger(0);
    }

    public void update(long totalLatency, long totalQueueTime, long totalProcessTime) {
        this.totalLatency.addAndGet(totalLatency);
        this.totalQueueTime.addAndGet(totalQueueTime);
        this.totalProcessTime.addAndGet(totalProcessTime);
    }

    public void incrementResultSize() {
        this.resultSize.incrementAndGet();
    }

    public void report() {
        System.out.println(this.toString());
    }

    public String toString() {
     return "Total latency: " + this.totalLatency +
             ", Total Queue Time: " + this.totalQueueTime +
             ", Total Process Time: " + this.totalProcessTime;
    }

    public int getResultSize() {
        return resultSize.get();
    }
}
