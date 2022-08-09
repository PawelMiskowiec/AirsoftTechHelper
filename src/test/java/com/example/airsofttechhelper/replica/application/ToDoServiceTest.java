package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.example.airsofttechhelper.replica.application.port.ToDoUseCase.CreateToDoCommand;
import com.example.airsofttechhelper.replica.db.ToDoJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ToDoServiceTest {

    @Autowired
    ToDoService toDoService;
    @Autowired
    UserEntityRepository userEntityRepository;
    @Autowired
    BasicReplicaService basicReplicaService;
    @Autowired
    ToDoJpaRepository toDoJpaRepository;

    @Test
    public void addToDoTest() {
        //given
        UserDetails userDetails = givenUserDetails("test@user.org");
        Replica replica = givenReplica("Test replica", userDetails.getUsername(), userDetails);
        CreateToDoCommand command = new CreateToDoCommand("test ToDO", "test content", replica.getId(), userDetails);

        //when
        ToDo toDo = toDoService.addToDo(command);

        //then
        assertEquals(toDo.getTitle(), toDoJpaRepository.findById(toDo.getId()).get().getTitle());
        assertEquals(toDo, toDoJpaRepository.findById(toDo.getId()).get());

    }

    private UserDetails givenUserDetails(String userMail) {
        UserEntity userEntity = new UserEntity(userMail, "123");
        userEntityRepository.save(userEntity);
        return new UserEntityDetails(userEntity);
    }

    private Replica givenReplica(String replicaName, String ownerEmail, UserDetails userDetails) {
        BasicReplicaUseCase.CreateOwnerCommand ownerCommand = toCreateOwnerCommand(ownerEmail);
        BasicReplicaUseCase.CreateReplicaCommand command = new BasicReplicaUseCase.CreateReplicaCommand(
                replicaName,
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand,
                userDetails
        );
        return basicReplicaService.addReplica(command);
    }

    private BasicReplicaUseCase.CreateOwnerCommand toCreateOwnerCommand(String email) {
        return new BasicReplicaUseCase.CreateOwnerCommand(
                "Pawel", "7123123", "example 12/3",
                "City", "11-123", email
        );
    }

}