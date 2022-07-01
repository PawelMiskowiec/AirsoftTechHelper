package com.example.airsofttechhelper.replica.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum ReplicaStatus {
    NEW{
        @Override
        public ReplicaStatus changeStatus(ReplicaStatus newStatus){
            return switch (newStatus){
                case NEW -> NEW;
                case INPROGRESS -> INPROGRESS;
                case TESTING -> TESTING;
                default -> super.changeStatus(newStatus);
            };
        }

    }, INPROGRESS{
        @Override
        public ReplicaStatus changeStatus(ReplicaStatus newStatus){
            return switch (newStatus){
                case INPROGRESS -> INPROGRESS;
                case TESTING -> TESTING;
                default -> super.changeStatus(newStatus);
            };
        }
    }, TESTING{
        @Override
        public ReplicaStatus changeStatus(ReplicaStatus newStatus){
            return switch (newStatus){
                case TESTING -> TESTING;
                case FINISHED -> FINISHED;
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
        throw new IllegalArgumentException("Unable to mark " + this.name() + " replica as " + newStatus.name());
    }
}
