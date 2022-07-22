package com.example.airsofttechhelper.replica;

import com.example.airsofttechhelper.replica.application.ReplicaListService;
import com.example.airsofttechhelper.replica.application.port.ReplicaListUseCase;
import com.example.airsofttechhelper.replica.db.OwnerJpaRepository;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static com.example.airsofttechhelper.replica.application.port.ReplicaListUseCase.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReplicaListServiceTest {

    @Autowired
    ReplicaJpaRepository replicaJpaRepository;

    @Autowired
    OwnerJpaRepository ownerJpaRepository;

    @Autowired
    ReplicaListService replicaListService;

    @Test
    public void userCanAddReplica(){
        //given
        CreateOwnerCommand ownerCommand = toCreateOwnerCommand("pawel@owner.com");
        CreateReplicaCommand command = new CreateReplicaCommand(
                "GG tr16 308 sr",
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand
                );

        //when
        Replica replica = replicaListService.addReplica(command);

        //then
        assertEquals(replica, replicaJpaRepository.findById(replica.getId()).get());

    }

    @Test
    public void userCanChangeReplicaStatusFromNewToInProgress(){
        //given
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");
        UpdateStatusCommand command = new UpdateStatusCommand(replica.getId(), "INPROGRESS");

        //when
        replicaListService.updateReplicaStatus(command);

        //then
        assertEquals(ReplicaStatus.INPROGRESS, replicaJpaRepository.findById(replica.getId()).get().getStatus());

    }

    @Test
    public void userCannotChangeFinishedReplicaStatus(){
        //given
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");
        UpdateStatusCommand testingCommand = new UpdateStatusCommand(replica.getId(), "TESTING");
        UpdateStatusCommand finishedCommand = new UpdateStatusCommand(replica.getId(), "FINISHED");

        //when
        replicaListService.updateReplicaStatus(testingCommand);
        replicaListService.updateReplicaStatus(finishedCommand);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            replicaListService.updateReplicaStatus(testingCommand);
        });

        //then
        Assertions.assertTrue(exception.getMessage().contains("Unable to change the replica status from FINISHED to TESTING"));
    }

    @Test
    public void userCannotChangeInProgressReplicaStatusToNew(){
        //given
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com");
        UpdateStatusCommand newCommand = new UpdateStatusCommand(replica.getId(), "NEW");
        UpdateStatusCommand inProgressCommand = new UpdateStatusCommand(replica.getId(), "INPROGRESS");

        //when
        replicaListService.updateReplicaStatus(inProgressCommand);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            replicaListService.updateReplicaStatus(newCommand);
        });

        //then
        Assertions.assertTrue(exception.getMessage().contains("Unable to change the replica status from INPROGRESS to NEW"));
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
