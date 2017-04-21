package com.linkedin.pid;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ywang2 on 10/4/16.
 */
public class Client implements Runnable {
  int clientId;
  Server[] serverpool;

  public Client(int id, Server[] servers) {
    clientId = id;
    serverpool = servers;
  }

  // keep generating requests and assign them to servers based on
  // different strategy
  public void run() {
    System.out.println("Client " + clientId + " started");
    try {
      while (true) {
        Request req = new Request();
        Server s = pendingRequestPickServer();
        s.q.add(req);
        long waitInterval = ThreadLocalRandom.current().nextLong(10);
        //System.out.println("Client " + clientId + " added request to server " + s.serverId);
        Thread.sleep(waitInterval);
      }
    } catch (Exception e) {
      System.out.println("Client " + clientId + " caught exception: " + e.toString());
    }
    System.out.println("Client " + clientId + " ended");
  }

  public Server randomPickServer() {
    int sId = ThreadLocalRandom.current().nextInt(serverpool.length);
    return serverpool[sId];
  }


  static int rrp = 0;
  public Server roundRobinPickServer() {
    rrp = (rrp + 1) % serverpool.length;
    return serverpool[rrp];
  }

  // pick the one with smallest pending request
  public Server pendingRequestPickServer() {
    int p = Integer.MAX_VALUE;
    Server ret = null;
    for (Server s : serverpool) {
      if (s.q.size() < p) {
        ret = s;
        p = s.q.size();
      }
    }
    return ret;
  }
}
