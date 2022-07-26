package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.part.web.ReplicaPartsController;
import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.application.port.ToDoUseCase;
import com.example.airsofttechhelper.replica.web.dto.RestBasicReplica;
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
class BasicReplicasControllerIT {
    @Autowired
    BasicReplicaUseCase replicaService;

    @Autowired
    BasicReplicasController basicReplicasController;

    @Autowired
    ReplicaPartsController replicaPartsController;

    @Autowired
    ReplicaPartUseCase replicaPartService;

    @Autowired
    ToDoUseCase toDoService;


    //Use of transactional resulted in non-deterministic test execution as the database had some additional replica
    //from method using dirtiesContext
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void  getAllReplicas(){
        //given
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");
        Replica replica2 = givenReplica("EA m4", "pawel@miskowiec.com");

        //when
        List<RestBasicReplica> all = basicReplicasController.getAllReplicas(Optional.empty());

        //then
        Assertions.assertEquals(2, all.size());
    }


    private Replica givenReplica(String replicaName, String ownerEmail){
        BasicReplicaUseCase.CreateOwnerCommand ownerCommand = toCreateOwnerCommand(ownerEmail);
        BasicReplicaUseCase.CreateReplicaCommand command = new BasicReplicaUseCase.CreateReplicaCommand(
                replicaName,
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand
        );
        return replicaService.addReplica(command);
    }

    private BasicReplicaUseCase.CreateOwnerCommand toCreateOwnerCommand(String email) {
        return new BasicReplicaUseCase.CreateOwnerCommand(
                "Pawel", "7123123", "example 12/3",
                "City", "11-123", email
        );
    }

}