package com.linkedin.pid.simulator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scho on 10/10/16.
 */
public class Client {
    private LoadBalancer<Integer> loadBalancer;
    private AtomicInteger counter;
    private Stats stats;

    public Client(LoadBalancer<Integer> loadBalancer, Stats stats) {
        counter = new AtomicInteger(0);
        this.loadBalancer = loadBalancer;
        this.stats = stats;
    }

    public void sendRequest(Request request) {
        //System.out.println("send request: " + request.getCost());
        request.send(this, loadBalancer.pickServer(request.getRequestId()));
    }

    public void handleResponse(Request request) {
        stats.incrementResultSize();
        request.wrap();
        //System.out.println(request.toString());
    }

    public void handleError(Request request) {
        stats.incrementResultSize();
        //System.out.println(request.toString());
    }
}
