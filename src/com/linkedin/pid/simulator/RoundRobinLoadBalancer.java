package com.linkedin.pid.simulator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scho on 10/10/16.
 */
public class RoundRobinLoadBalancer<T> extends AbstractLoadBalancer implements LoadBalancer<T>{
    private AtomicInteger counter;

    public RoundRobinLoadBalancer (List<Server> serverList) {
        counter = new AtomicInteger(0);
        super.serverList = serverList;
    }

    public Server pickServer(T integer) {
        counter.weakCompareAndSet(serverList.size(), 0);
        int bucket = counter.getAndIncrement() % serverList.size();
        return serverList.get(bucket);
    }
}
