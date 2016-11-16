package com.linkedin.pid.simulator;

import java.util.List;

/**
 * Created by scho on 10/10/16.
 */
public interface LoadBalancer<PartitionId> {
    public Server pickServer(PartitionId partitionId);
}
