package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.part.application.ReplicaPartService;
import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.part.domain.ReplicaPart;
import com.example.airsofttechhelper.replica.application.ReplicaListService;
import com.example.airsofttechhelper.replica.application.ReplicaService;
import com.example.airsofttechhelper.replica.application.port.ReplicaListUseCase;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.todo.application.ToDoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
class DetailedReplicaControllerIT {

    @Autowired
    DetailedReplicaController controller;

    @Autowired
    ReplicaPartService replicaPartService;

    @Autowired
    ToDoService toDoService;

    @Autowired
    ReplicaService replicaService;

    @Autowired
    ReplicaListService replicaListService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getByIdTest(){ //Just playing with hql, lazy initialization, hashCode & equals
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

        replicaService.findById(replica.getId()) //this method manages to fetch all associated entities with one query
                .getParts().forEach(restReplicaPart -> System.out.println(restReplicaPart.getName()));
        System.out.println(replicaPart.hashCode() == replicaPart2.hashCode());
        System.out.println(replicaPart.equals(replicaPart2));
    }

    private Replica givenReplica(String replicaName, String ownerEmail){
        ReplicaListUseCase.CreateOwnerCommand ownerCommand = toCreateOwnerCommand(ownerEmail);
        ReplicaListUseCase.CreateReplicaCommand command = new ReplicaListUseCase.CreateReplicaCommand(
                replicaName,
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand
        );
        return replicaListService.addReplica(command);
    }

    private ReplicaListUseCase.CreateOwnerCommand toCreateOwnerCommand(String email) {
        return new ReplicaListUseCase.CreateOwnerCommand(
                "Pawel", "7123123", "example 12/3",
                "City", "11-123", email
        );
    }
}