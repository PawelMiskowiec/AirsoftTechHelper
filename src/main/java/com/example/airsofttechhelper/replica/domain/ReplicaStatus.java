package com.example.airsofttechhelper.replica.domain;

import java.util.Arrays;
import java.util.Optional;

public enum ReplicaStatus {
    NEW{
        @Override
        public ReplicaStatus changeStatus(ReplicaStatus newStatus){
            return switch (newStatus){
                case NEW, TESTING, INPROGRESS -> newStatus;
                default -> super.changeStatus(newStatus);
            };
        }

    }, INPROGRESS{
        @Override
        public ReplicaStatus changeStatus(ReplicaStatus newStatus){
            return switch (newStatus){
                case INPROGRESS, TESTING -> newStatus;
                default -> super.changeStatus(newStatus);
            };
        }
    }, TESTING{
        @Override
        public ReplicaStatus changeStatus(ReplicaStatus newStatus){
            return switch (newStatus){
                case INPROGRESS, TESTING, FINISHED -> newStatus;
                default -> super.changeStatus(newStatus);
            };
        }
    }, FINISHED;

    public static Optional<ReplicaStatus> parseString(String status){
        return Arrays
                .stream(values())
                .filter(s -> status.equalsIgnoreCase(s.name()))
                .findFirst();
    }
    public ReplicaStatus changeStatus(ReplicaStatus newStatus){
        throw new IllegalArgumentException("Unable to change the replica status from " + this.name() + " to " + newStatus.name());
    }
}
