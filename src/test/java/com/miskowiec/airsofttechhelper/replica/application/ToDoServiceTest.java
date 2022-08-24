package com.miskowiec.airsofttechhelper.replica.application;

import com.miskowiec.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.miskowiec.airsofttechhelper.replica.application.port.ToDoUseCase.CreateToDoCommand;
import com.miskowiec.airsofttechhelper.replica.db.ToDoJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.replica.domain.ToDo;
import com.miskowiec.airsofttechhelper.security.UnauthorizedAccessException;
import com.miskowiec.airsofttechhelper.security.UserEntityDetails;
import com.miskowiec.airsofttechhelper.user.db.UserEntityJpaRepository;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import org.junit.jupiter.api.Assertions;
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
    UserEntityJpaRepository userEntityJpaRepository;
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

    @Test
    public void userCannotAddToDoToReplicaHeDoesNotOwn(){
        //given
        UserDetails userDetails = givenUserDetails("test@user.org");
        UserDetails unauthorisedTech = givenUserDetails("unauthorised@user.org");
        Replica replica = givenReplica("Test replica", userDetails.getUsername(), userDetails);
        CreateToDoCommand command = new CreateToDoCommand("test ToDO", "test content", replica.getId(), unauthorisedTech);
        String expectedExMessage = unauthorisedTech.getUsername() + " is not authorised to add todos to Replica with id " + replica.getId();

        //when
        UnauthorizedAccessException exception = Assertions.assertThrows(UnauthorizedAccessException.class, () -> {
                toDoService.addToDo(command);
        });

        //then
        assertEquals(expectedExMessage, exception.getMessage());
    }

    @Test
    public void userCannotAddToDoToNonExistingReplica(){
        UserDetails userDetails = givenUserDetails("test@user.org");
        CreateToDoCommand command = new CreateToDoCommand("test ToDO", "test content", 1l, userDetails);
        String expectedExMessage = "Replica not found";

        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            toDoService.addToDo(command);
        });

        //then
        assertEquals(expectedExMessage, exception.getMessage());
    }

    private UserDetails givenUserDetails(String userMail) {
        UserEntity userEntity = new UserEntity(userMail, "123");
        userEntityJpaRepository.save(userEntity);
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