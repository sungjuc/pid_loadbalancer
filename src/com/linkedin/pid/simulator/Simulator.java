package com.linkedin.pid.simulator;

import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scho on 10/10/16.
 */
public class Simulator {
    public static final int QPS = 300;
    public static final int TEST_SIZE = 1000;
    public static final int TOTAL_SERVERS = 5;

    public static void main(String args[]) throws InterruptedException {
        testVariableQPS();

    }

    public static void testVariableQPS() throws InterruptedException {
        String results = "";
        System.out.println("Start Simulator");
        List<Server> serverList = MultiThreadedServer.CreateServerList(TOTAL_SERVERS);
        System.out.println("Servers are Ready!");

        for (int i = 0; i < 3; i++) {
            int testQPS = QPS + 10 * i;
            System.out.println("-----------------------------------------------------");
            LoadBalancer<Integer> RLB = new RandomLoadBalancer<Integer>(serverList);
            results += "RDLB\t" + testQPS + "\t" + testRunner(RLB, testQPS) + "\n";
            LoadBalancer<Integer> RRLB = new RoundRobinLoadBalancer<Integer>(serverList);
            results += "RRLB\t" + testQPS + "\t" + testRunner(RRLB, testQPS) + "\n";
            LoadBalancer<Integer> PRLB = new QueueLengthLoadBalancer<Integer>(serverList);
            results += "PRLB\t" + testQPS + "\t" + testRunner(PRLB, testQPS) + "\n";
            LoadBalancer<Integer> PCLB = new PendingCostLoadBalancer<Integer>(serverList, 30);
            results += "PCLB\t" + testQPS + "\t" + testRunner(PCLB, testQPS) + "\n";
            results += "----------------------------------------------------------\n";
            System.out.println("-----------------------------------------------------");
        }
        System.out.println(results);
        System.out.println("Done");

        for (Server server : serverList) {
            server.shutdown();
        }
    }

    public static String testRunner(LoadBalancer<Integer> lb, int QPS) throws InterruptedException {
        Stats stats = new Stats();

        Client client = new Client(lb, stats);
        System.out.println("Start Sending Reuqests!");

        RequestGenerator requestGenerator = new RequestGenerator(QPS, TEST_SIZE, client, stats);
        requestGenerator.start();


        while (stats.getResultSize() != TEST_SIZE) {
            System.out.println("Still processing: " + stats.getResultSize() + "/" + TEST_SIZE);
            Thread.sleep(1000);
        }

        return stats.toString();
    }


}
