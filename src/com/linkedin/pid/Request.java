package com.linkedin.pid;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ywang2 on 10/3/16.
 */
public class Request {
  public Request() {
    startTs = System.currentTimeMillis();
    id = ThreadLocalRandom.current().nextInt();
  }


  int id;

  // time when request is issued by client
  long startTs;
}
