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
    private AtomicInteger errors;

    public Stats() {
        init();
    }

    public void init() {
        totalLatency = new AtomicLong(0);
        totalQueueTime = new AtomicLong(0);
        totalProcessTime = new AtomicLong(0);
        resultSize = new AtomicInteger(0);
        errors = new AtomicInteger(0);
    }

    public void update(long totalLatency, long totalQueueTime, long totalProcessTime, boolean success) {
        this.totalLatency.addAndGet(totalLatency);
        if (success) {
            this.totalQueueTime.addAndGet(totalQueueTime);
            this.totalProcessTime.addAndGet(totalProcessTime);
        } else {
            errors.incrementAndGet();
        }
    }

    public AtomicLong getTotalLatency() {
        return this.totalLatency;
    }

    public AtomicLong getTotalQueueTime() {
        return this.totalQueueTime;
    }

    public AtomicLong getTotalProcessTime() {
        return this.totalProcessTime;
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
             ", Total Process Time: " + this.totalProcessTime +
             ", Total Errors: " + this.errors;
    }

    public int getResultSize() {
        return resultSize.get();
    }
}
