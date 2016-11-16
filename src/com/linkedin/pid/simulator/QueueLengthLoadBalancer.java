package com.linkedin.pid.simulator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scho on 10/10/16.
 */
public class QueueLengthLoadBalancer<T> extends AbstractLoadBalancer implements LoadBalancer<T>{
    private AtomicInteger counter;

    public QueueLengthLoadBalancer(List<Server> serverList) {
        counter = new AtomicInteger(0);
        super.serverList = serverList;
    }

    public Server pickServer(T integer) {
        Server bestServer = null;
        for (Server server: serverList) {
            if (bestServer == null) {
                bestServer = server;
            } else {
                bestServer = (bestServer.getPendingRequests() < server.getPendingRequests())? bestServer: server;
            }

            if (bestServer.getPendingRequests() < 3) {
                return bestServer;
            }

        }
        return bestServer;
    }
}
