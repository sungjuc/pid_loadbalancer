package com.linkedin.pid.simulator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scho on 10/10/16.
 */
public class PendingCostLoadBalancer<T> extends AbstractLoadBalancer implements LoadBalancer<T>{
    private AtomicInteger counter;
    int costThreshold = 0;

    public PendingCostLoadBalancer(List<Server> serverList, int costThreshold) {
        counter = new AtomicInteger(0);
        super.serverList = serverList;
        this.costThreshold = costThreshold;
    }

    public Server pickServer(T integer) {
        Server bestServer = null;
        for (Server server: serverList) {
            if (bestServer == null) {
                bestServer = server;
            } else {
                bestServer = (bestServer.getPendingCosts() < server.getPendingCosts())? bestServer: server;
            }

            if (bestServer.getPendingRequests() < 3 || bestServer.getPendingCosts() < costThreshold) {
                return bestServer;
            }

        }
        return bestServer;
    }
}
