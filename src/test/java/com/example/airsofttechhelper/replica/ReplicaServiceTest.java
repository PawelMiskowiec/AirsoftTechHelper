package com.example.airsofttechhelper.replica;

import com.example.airsofttechhelper.replica.application.ReplicaService;
import com.example.airsofttechhelper.replica.db.OwnerRepository;
import com.example.airsofttechhelper.replica.db.ReplicaRepository;
import com.example.airsofttechhelper.replica.domain.Owner;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static com.example.airsofttechhelper.replica.application.port.ReplicaUseCase.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReplicaServiceTest {

    @Autowired
    ReplicaRepository replicaRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    ReplicaService replicaService;

    @Test
    public void userCanAddReplica(){
        //given
        Owner owner = givenOwner("Pawel");
        CreateOwnerCommand ownerCommand = toCreateOwnerCommand(owner);
        CreateReplicaCommand command = new CreateReplicaCommand(
                "GG tr16 308 sr",
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand
                );

        //when
        Replica replica = replicaService.addReplica(command);

        //then
        assertEquals(replica, replicaRepository.findById(replica.getId()).get());

    }

    @Test
    public void userCanChangeReplicaStatusFromNewToInProgress(){
        //given
        Replica replica = givenReplica();
        UpdateStatusCommand command = new UpdateStatusCommand(replica.getId(), ReplicaStatus.INPROGRESS);

        //when
        replicaService.updateReplicaStatus(command);

        //then
        assertEquals(ReplicaStatus.INPROGRESS, replicaRepository.findById(replica.getId()).get().getStatus());

    }

    @Test
    public void userCannotChangeFinishedReplicaStatus(){
        //given
        Replica replica = givenReplica();
        UpdateStatusCommand testingCommand = new UpdateStatusCommand(replica.getId(), ReplicaStatus.TESTING);
        UpdateStatusCommand finishedCommand = new UpdateStatusCommand(replica.getId(), ReplicaStatus.FINISHED);

        //when
        replicaService.updateReplicaStatus(testingCommand);
        replicaService.updateReplicaStatus(finishedCommand);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            replicaService.updateReplicaStatus(testingCommand);
        });

        //then
        Assertions.assertTrue(exception.getMessage().contains("Unable to change the replica status from FINISHED to TESTING"));
    }

    @Test
    public void userCannotChangeInProgressReplicaStatusToNew(){
        //given
        Replica replica = givenReplica();
        UpdateStatusCommand newCommand = new UpdateStatusCommand(replica.getId(), ReplicaStatus.NEW);
        UpdateStatusCommand inProgressCommand = new UpdateStatusCommand(replica.getId(), ReplicaStatus.INPROGRESS);

        //when
        replicaService.updateReplicaStatus(inProgressCommand);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            replicaService.updateReplicaStatus(newCommand);
        });

        //then
        Assertions.assertTrue(exception.getMessage().contains("Unable to change the replica status from INPROGRESS to NEW"));
    }

    private Replica givenReplica(){
        Owner owner = givenOwner("Pawel");
        CreateOwnerCommand ownerCommand = toCreateOwnerCommand(owner);
        CreateReplicaCommand command = new CreateReplicaCommand(
                "GG tr16 308 sr",
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand
        );
        return replicaService.addReplica(command);
    }

    private CreateOwnerCommand toCreateOwnerCommand(Owner owner) {
        return new CreateOwnerCommand(
                owner.getName(),
                owner.getPhone(),
                owner.getStreet(),
                owner.getCity(),
                owner.getZipCode(),
                owner.getEmail()
            );
    }


    private Owner givenOwner(String name){
        return ownerRepository.save(new Owner(name, "7123123", "example 12/3",
                "City", "11-123", "owner@mail.com"));
    }

}
