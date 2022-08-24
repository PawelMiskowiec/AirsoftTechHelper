package com.miskowiec.airsofttechhelper.replica.application;

import com.miskowiec.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.miskowiec.airsofttechhelper.replica.db.OwnerJpaRepository;
import com.miskowiec.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.replica.domain.ReplicaStatus;
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

import java.util.List;

import static com.miskowiec.airsofttechhelper.replica.application.port.BasicReplicaUseCase.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicReplicaServiceTest {

    @Autowired
    ReplicaJpaRepository replicaJpaRepository;

    @Autowired
    OwnerJpaRepository ownerJpaRepository;

    @Autowired
    BasicReplicaService basicReplicaService;

    @Autowired
    UserEntityJpaRepository userEntityJpaRepository;

    @Test
    public void userCanAddReplica() {
        //given
        UserDetails userDetails = givenUserDetails("example@tech.com");
        CreateOwnerCommand ownerCommand = toCreateOwnerCommand("pawel@replicaOwner.com");
        CreateReplicaCommand command = new CreateReplicaCommand(
                "GG tr16 308 sr",
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand,
                userDetails
        );

        //when
        Replica replica = basicReplicaService.addReplica(command);

        //then
        assertEquals(replica, replicaJpaRepository.findById(replica.getId()).get());

    }

    @Test
    public void userCannotChangeReplicaStatusToNonexistent(){
        //given
        UserDetails userDetails = givenUserDetails("example@tech.com");
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com", userDetails);
        UpdateReplicaStatusCommand command = new UpdateReplicaStatusCommand(replica.getId(), "faultyStatus", userDetails);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            basicReplicaService.updateReplicaStatus(command);
        });

        //then
        assertEquals(ReplicaStatus.NEW, replicaJpaRepository.findById(replica.getId()).get().getStatus());
        assertEquals(exception.getMessage(), "faultyStatus is not a valid replica status");
    }

    @Test
    public void userCanChangeReplicaStatusFromNewToInProgress() {
        //given
        UserDetails userDetails = givenUserDetails("example@tech.com");
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com", userDetails);
        UpdateReplicaStatusCommand command = new UpdateReplicaStatusCommand(replica.getId(), "INPROGRESS", userDetails);

        //when
        basicReplicaService.updateReplicaStatus(command);

        //then
        assertEquals(ReplicaStatus.INPROGRESS, replicaJpaRepository.findById(replica.getId()).get().getStatus());

    }

    @Test
    public void userCannotChangeFinishedReplicaStatus() {
        //given
        UserDetails userDetails = givenUserDetails("example@tech.com");
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com", userDetails);
        UpdateReplicaStatusCommand testingCommand = new UpdateReplicaStatusCommand(replica.getId(), "TESTING", userDetails);
        UpdateReplicaStatusCommand finishedCommand = new UpdateReplicaStatusCommand(replica.getId(), "FINISHED", userDetails);

        //when
        basicReplicaService.updateReplicaStatus(testingCommand);
        basicReplicaService.updateReplicaStatus(finishedCommand);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            basicReplicaService.updateReplicaStatus(testingCommand);
        });

        //then
        Assertions.assertTrue(exception.getMessage().contains("Unable to change the replica status from FINISHED to TESTING"));
    }

    @Test
    public void userCannotChangeInProgressReplicaStatusToNew() {
        //given
        UserDetails userDetails = givenUserDetails("example@tech.com");
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com", userDetails);
        UpdateReplicaStatusCommand newCommand = new UpdateReplicaStatusCommand(replica.getId(), "NEW", userDetails);
        UpdateReplicaStatusCommand inProgressCommand = new UpdateReplicaStatusCommand(replica.getId(), "INPROGRESS", userDetails);

        //when
        basicReplicaService.updateReplicaStatus(inProgressCommand);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            basicReplicaService.updateReplicaStatus(newCommand);
        });

        //then
        Assertions.assertTrue(exception.getMessage().contains("Unable to change the replica status from INPROGRESS to NEW"));
    }

    @Test
    public void userShouldReceiveOnlyOwnedReplicas(){
        //given
        UserDetails firstUser = givenUserDetails("example@tech.com");
        UserDetails secondUser = givenUserDetails("secondExample@tech.com");
        givenReplica("Tr16 test", firstUser.getUsername(), firstUser);
        Replica replica2 = givenReplica("Tr16 test replica no 2", firstUser.getUsername(), firstUser);

        //when
        List<Replica> firstUserReplicas = basicReplicaService.findAllUserReplicas(firstUser.getUsername());
        List<Replica> secondUserReplicas = basicReplicaService.findAllUserReplicas(secondUser.getUsername());

        //then
        assertEquals(2, firstUserReplicas.size());
        assertEquals(0, secondUserReplicas.size());
        assertTrue(firstUserReplicas.contains(replica2));
        assertFalse(secondUserReplicas.contains(replica2));
    }

    @Test
    public void shouldGetAllUserReplicasWithStatusInProgress(){
        //given
        UserDetails user = givenUserDetails("example@tech.com");
        givenReplica("Tr16 test", user.getUsername(), user);
        Replica replica2 = givenReplica("Tr16 test replica no 2", user.getUsername(), user);
        UpdateReplicaStatusCommand command = new UpdateReplicaStatusCommand(replica2.getId(), "INPROGRESS", user);
        basicReplicaService.updateReplicaStatus(command);

        //when
        List<Replica> allInProgress = basicReplicaService.findAllUserReplicasByStatus("inprogress", user.getUsername());

        //then
        assertEquals(1, allInProgress.size());
        assertTrue(allInProgress.contains(replica2));
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
