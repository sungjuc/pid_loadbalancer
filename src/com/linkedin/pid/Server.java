package com.linkedin.pid;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ywang2 on 10/3/16.
 * <p>
 * keep running incoming requests with different latencies.
 */
public class Server implements Runnable {

  int serverId;
  BlockingQueue<Request> q = new LinkedBlockingDeque<>();


  // latency store, need more logic
  long[] latencyStore = new long[10000];

  public Server(int id) {
    serverId = id;
  }

  public void run() {
    System.out.println("Server " + serverId + " started");

    try {
      while (true) {
        Request req = q.take();
        long latency = genLatency();
        Thread.sleep(latency);
        long elapse = System.currentTimeMillis() - req.startTs;
        System.out.println("Server " + serverId + " processed request in " + elapse + "ms. CPU time is " + latency + "ms. "
                + "Current pending queue size: " + q.size() + ". CPU usage ratio: " + ((double) aggrCPU / aggrTotal));
        updateLatencyStore(elapse, req.startTs);
        updateCPURatio(latency, elapse);
      }
    } catch (Exception e) {
      System.out.println("Server " + serverId + " caught exception: " + e.toString());
    }
    System.out.println("Server " + serverId + " ended");
  }

  public void updateLatencyStore(long elapse, long start) {
    latencyStore[(int) (start % 10000)] = elapse;
  }

  // reporting of actual time spent on processing request
  long aggrTotal = 1;
  long aggrCPU = 1;
  public void updateCPURatio(long cpu, long total) {
    aggrTotal += total;
    aggrCPU += cpu;
  }

  // allow different latency distributions
  public long genLatency() {
    return genGaussian();
  }
  public long genRandom() {
    return ThreadLocalRandom.current().nextLong(100);
  }
  public long genGaussian() {
    int g = 1 + (int) (10 * Math.abs(ThreadLocalRandom.current().nextGaussian()));
    return g;
  }
}
