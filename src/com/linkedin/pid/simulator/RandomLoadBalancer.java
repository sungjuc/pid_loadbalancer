package com.linkedin.pid.simulator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scho on 10/10/16.
 */
public class RandomLoadBalancer<T> extends AbstractLoadBalancer implements LoadBalancer<T>{
    private AtomicInteger counter;
    private Random random;

    public RandomLoadBalancer(List<Server> serverList) {
        counter = new AtomicInteger(0);
        super.serverList = serverList;
        random = new Random(12345L);
    }

    public Server pickServer(T integer) {
        //Collections.shuffle(super.serverList, random);
        return serverList.get(random.nextInt(serverList.size()));
    }
}
