package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.db.OwnerJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReplicasControllerIT {
    @Autowired
    ReplicaUseCase replicaService;

    @Autowired
    OwnerJpaRepository ownerJpaRepository;

    @Autowired
    ReplicasController replicasController;


    @Test
    public void  getAllReplicas(){
        //given
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");
        Replica replica2 = givenReplica("EA m4", "pawel@miskowiec.com");

        //when
        List<RestMinReplica> all = replicasController.getAllReplicas(Optional.empty());

        //then
        Assertions.assertEquals(all.size(), 2);
    }

    private Replica givenReplica(String replicaName, String ownerEmail){
        ReplicaUseCase.CreateOwnerCommand ownerCommand = toCreateOwnerCommand(ownerEmail);
        ReplicaUseCase.CreateReplicaCommand command = new ReplicaUseCase.CreateReplicaCommand(
                replicaName,
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand
        );
        return replicaService.addReplica(command);
    }

    private ReplicaUseCase.CreateOwnerCommand toCreateOwnerCommand(String email) {
        return new ReplicaUseCase.CreateOwnerCommand(
                "Pawel", "7123123", "example 12/3",
                "City", "11-123", email
        );
    }

}