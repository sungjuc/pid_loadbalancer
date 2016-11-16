package com.linkedin.pid.simulator;

import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scho on 10/10/16.
 */
public class Simulator {
    public static final int QPS = 100;
    public static final int TEST_SIZE = 10000;
    public static final int TOTAL_SERVERS = 5;

    public static void main(String args[]) throws InterruptedException {
        String results = "";
        System.out.println("Start Simulator");
        List<Server> serverList = MultiThreadedServer.CreateServerList(TOTAL_SERVERS);
        System.out.println("Servers are Ready!");

        LoadBalancer<Integer> RLB = new RandomLoadBalancer<Integer>(serverList);
        results += "RLB:\t"  + testRunner(RLB) + "\n";
        LoadBalancer<Integer> RRLB = new RoundRobinLoadBalancer<Integer>(serverList);
        results += "RRLB:\t"  + testRunner(RRLB) + "\n";
        LoadBalancer<Integer> PRLB = new QueueLengthLoadBalancer<Integer>(serverList);
        results += "PRLB:\t"  + testRunner(PRLB) + "\n";
        LoadBalancer<Integer> PCLB = new PendingCostLoadBalancer<Integer>(serverList);
        results += "PCLB:\t"  + testRunner(PCLB) + "\n";
        System.out.println("Done");

        System.out.println(results);

        for (Server server : serverList) {
            server.shutdown();
        }

    }

    public static String testRunner(LoadBalancer<Integer> lb) throws InterruptedException{
        Stats stats = new Stats();

        Client client = new Client(lb, stats);
        System.out.println("Start Sending Reuqests!");

        RequestGenerator requestGenerator = new RequestGenerator(QPS, TEST_SIZE, client, stats);
        requestGenerator.start();


        while (stats.getResultSize() != TEST_SIZE) {
            System.out.println("Still processing: " + stats.getResultSize() + "/" +TEST_SIZE);
            Thread.sleep(1000);
        }

        return stats.toString();
    }

}
