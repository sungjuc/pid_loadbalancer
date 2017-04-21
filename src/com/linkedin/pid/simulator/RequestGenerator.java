package com.linkedin.pid.simulator;

import java.util.Random;

/**
 * Created by scho on 10/21/16.
 */
public class RequestGenerator implements Runnable {
    private int generatedRequests;
    private int numRequests;
    private long interval_mili;
    private int interval_nano;
    private Client client;
    private Random random;
    private Stats stats;

    public RequestGenerator(int qps, int numRequests, Client client, Stats stats) {
        generatedRequests = 0;
        this.numRequests = numRequests;
        this.client = client;
        this.stats = stats;
        long interval = (1000L * 1000000) / qps;
        interval_mili = interval / 1000000;
        interval_nano =  (int)(interval % 1000000);
        System.out.println("Interval:" + interval + ", interval mili: " + interval_mili + ", interval nano: " + interval_nano);
        random = new Random(12345L);
    }

    @Override
    public void run() {
        for (int i = 0; i < numRequests; i++) {
            try {
                Thread.sleep(interval_mili, interval_nano);
                //System.out.println(numRequests + " : " + i);
                client.sendRequest(new Request(i, random.nextInt(200) + 2, stats));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
}
