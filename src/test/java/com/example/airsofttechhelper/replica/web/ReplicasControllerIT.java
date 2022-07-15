package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.part.domain.ReplicaPart;
import com.example.airsofttechhelper.part.web.ReplicaPartsController;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.db.OwnerJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.todo.application.port.ToDoUseCase;
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

    @Autowired
    ReplicaPartsController replicaPartsController;

    @Autowired
    ReplicaPartUseCase replicaPartService;

    @Autowired
    ToDoUseCase toDoService;


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

    @Test
    public void getFullReplicaWithReplicaTestParts(){
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");
        ReplicaPartUseCase.CreateReplicaPartCommand command =
                new ReplicaPartUseCase.CreateReplicaPartCommand(replica.getId(), Optional.empty(), "First Part",
                        "HopUp", "Very good part");
        ReplicaPartUseCase.CreateReplicaPartCommand command2 =
                new ReplicaPartUseCase.CreateReplicaPartCommand(replica.getId(), Optional.empty(), "Second Part",
                        "misc", "Very good part");
        ReplicaPart replicaPart = replicaPartService.addReplicaPart(command);
        ReplicaPart replicaPart2 = replicaPartService.addReplicaPart(command2);

        toDoService.addToDo("jakiesTam", "Zawartosc", replica.getId());



        replicaService.findOneByIdEager(replica.getId()).get()
                .getReplicaParts().forEach(replicaPart1 -> System.out.println(replicaPart1.getPart().getName()));
        System.out.println(replicaPart.hashCode() == replicaPart2.hashCode());


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