package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.part.web.ReplicaPartsController;
import com.example.airsofttechhelper.replica.application.port.ReplicaListUseCase;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.application.port.ToDoUseCase;
import com.example.airsofttechhelper.replica.web.dto.RestListReplica;
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
class ReplicasControllerIT {
    @Autowired
    ReplicaListUseCase replicaService;

    @Autowired
    ReplicasController replicasController;

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
        List<RestListReplica> all = replicasController.getAllReplicas(Optional.empty());

        //then
        Assertions.assertEquals(2, all.size());
    }


    private Replica givenReplica(String replicaName, String ownerEmail){
        ReplicaListUseCase.CreateOwnerCommand ownerCommand = toCreateOwnerCommand(ownerEmail);
        ReplicaListUseCase.CreateReplicaCommand command = new ReplicaListUseCase.CreateReplicaCommand(
                replicaName,
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand
        );
        return replicaService.addReplica(command);
    }

    private ReplicaListUseCase.CreateOwnerCommand toCreateOwnerCommand(String email) {
        return new ReplicaListUseCase.CreateOwnerCommand(
                "Pawel", "7123123", "example 12/3",
                "City", "11-123", email
        );
    }

}