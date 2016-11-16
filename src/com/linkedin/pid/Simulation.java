package com.linkedin.pid;

/**
 * Created by ywang2 on 10/4/16.
 */
public class Simulation {
  public void simululate(int clients, int servers) {
    System.out.println("Start simulation...");
    Server[] allservers = new Server[servers];
    Thread[] allserversThreads = new Thread[servers];

    Client[] allclients = new Client[clients];
    Thread[] allclientsThreads = new Thread[clients];

    for (int i = 0; i < allservers.length; i++) {
      allservers[i] = new Server(i);
      allserversThreads[i] = new Thread(allservers[i]);
    }

    for (int i = 0; i < allclients.length; i++) {
      allclients[i] = new Client(i, allservers);
      allclientsThreads[i] = new Thread(allclients[i]);
    }

    for (Thread t : allserversThreads) {
      t.start();
    }

    for (Thread t : allclientsThreads) {
      t.start();
    }

    try {
      for (Thread t : allserversThreads) {
        t.join();
      }

      for (Thread t : allclientsThreads) {
        t.join();
      }

    } catch (Exception e) {
      System.out.println("Caught exception: " + e.toString());
    }

  }
}
