package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.part.application.ReplicaPartService;
import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.part.domain.ReplicaPart;
import com.example.airsofttechhelper.replica.application.BasicBasicReplicaService;
import com.example.airsofttechhelper.replica.application.DetailedReplicaService;
import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.application.ToDoService;
import com.example.airsofttechhelper.replica.application.port.ToDoUseCase;
import com.example.airsofttechhelper.replica.domain.ToDo;
import com.example.airsofttechhelper.security.UserEntityDetails;
import com.example.airsofttechhelper.user.db.UserEntityRepository;
import com.example.airsofttechhelper.user.domain.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
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
    DetailedReplicaService replicaService;

    @Autowired
    BasicBasicReplicaService basicReplicaService;

    @Autowired
    UserEntityRepository userEntityRepository;

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
        ToDoUseCase.CreateToDoCommand createToDoCommand = new ToDoUseCase.CreateToDoCommand("ExampleToDo", "Example todoContent", replica.getId());
        ToDo toDo = toDoService.addToDo(createToDoCommand);
        System.out.println("\nUpdating todo\n");
        ToDoUseCase.UpdateToDoCommand updateToDoCommand = new ToDoUseCase.UpdateToDoCommand("Updated ExampleToDo", "toDoContent", toDo.getId());
        toDoService.updateToDO(updateToDoCommand);

        System.out.println("\n before printing parts \n");

        replicaService.findById(replica.getId()).get() //this method manages to fetch all associated entities with one query
                .getReplicaParts().forEach(replicaPart1 -> System.out.println(replicaPart1.getPart().getName()));
        System.out.println(replicaPart.hashCode() == replicaPart2.hashCode());
        System.out.println(replicaPart.equals(replicaPart2));
    }

    private Replica givenReplica(String replicaName, String ownerEmail){
        BasicReplicaUseCase.CreateOwnerCommand ownerCommand = toCreateOwnerCommand(ownerEmail);
        BasicReplicaUseCase.CreateReplicaCommand command = new BasicReplicaUseCase.CreateReplicaCommand(
                replicaName,
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand,
                givenUserDetails()
        );
        return basicReplicaService.addReplica(command);
    }

    private UserDetails givenUserDetails() {
        UserEntity userEntity = new UserEntity("example@tech.com", "123");
        userEntityRepository.save(userEntity);
        return new UserEntityDetails(userEntity);
    }

    private BasicReplicaUseCase.CreateOwnerCommand toCreateOwnerCommand(String email) {
        return new BasicReplicaUseCase.CreateOwnerCommand(
                "Pawel", "7123123", "example 12/3",
                "City", "11-123", email
        );
    }
}