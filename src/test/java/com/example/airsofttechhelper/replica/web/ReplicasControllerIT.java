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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
class ReplicasControllerIT {
    @Autowired
    ReplicaUseCase replicaService;

    @Autowired
    ReplicasController replicasController;

    @Autowired
    ReplicaPartsController replicaPartsController;

    @Autowired
    ReplicaPartUseCase replicaPartService;

    @Autowired
    ToDoUseCase toDoService;


    @Test
    @Transactional
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getFullReplicaTest(){ //Just playing with hql, lazy initialization, hashCode & equals
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");

        ReplicaPartUseCase.CreateReplicaPartCommand command =
                new ReplicaPartUseCase.CreateReplicaPartCommand(replica.getId(), Optional.empty(), "First Part",
                        "HopUp", "Very good part");
        ReplicaPartUseCase.CreateReplicaPartCommand command2 =
                new ReplicaPartUseCase.CreateReplicaPartCommand(replica.getId(), Optional.empty(), "Second Part",
                        "misc", "Very good part");

        System.out.println("\nAdding first part \n");
        ReplicaPart replicaPart = replicaPartService.addReplicaPart(command);
        System.out.println("\nAdding second part \n");
        ReplicaPart replicaPart2 = replicaPartService.addReplicaPart(command2);
        System.out.println("\nAdding todo \n");
        toDoService.addToDo("ExampleToDo", "Example todoContent", replica.getId());

        System.out.println("\n before printing parts \n");

        replicaService.findOneByIdEager(replica.getId()).get() //this method manages to fetch all associated entities with one query
                .getReplicaParts().forEach(replicaPart1 -> System.out.println(replicaPart1.getPart().getName()));
        System.out.println(replicaPart.hashCode() == replicaPart2.hashCode());
        System.out.println(replicaPart.equals(replicaPart2));


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