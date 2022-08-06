package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.example.airsofttechhelper.replica.db.OwnerJpaRepository;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import com.example.airsofttechhelper.security.UserEntityDetails;
import com.example.airsofttechhelper.user.db.UserEntityRepository;
import com.example.airsofttechhelper.user.domain.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;

import static com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BasicReplicaServiceTest {

    @Autowired
    ReplicaJpaRepository replicaJpaRepository;

    @Autowired
    OwnerJpaRepository ownerJpaRepository;

    @Autowired
    BasicBasicReplicaService basicReplicaService;

    @Autowired
    UserEntityRepository userEntityRepository;

    UserDetails userDetails;

    @BeforeEach
    private void prepareUserDetails() {
        UserEntity userEntity = new UserEntity("example@tech.com", "123");
        userEntityRepository.save(userEntity);
        this.userDetails = new UserEntityDetails(userEntity);
    }

    @Test
    public void userCanAddReplica() {
        //given
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
    public void userCanChangeReplicaStatusFromNewToInProgress() {
        //given
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");
        UpdateReplicaStatusCommand command = new UpdateReplicaStatusCommand(replica.getId(), "INPROGRESS", userDetails);

        //when
        basicReplicaService.updateReplicaStatus(command);

        //then
        assertEquals(ReplicaStatus.INPROGRESS, replicaJpaRepository.findById(replica.getId()).get().getStatus());

    }

    @Test
    public void userCannotChangeFinishedReplicaStatus() {
        //given
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");
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
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");
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

    private Replica givenReplica(String replicaName, String ownerEmail) {
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
