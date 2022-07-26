package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.web.dto.RestDetailedReplica;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReplicaServiceTest {

    @Autowired
    BasicBasicReplicaService basicReplicaService;

    @Autowired
    ReplicaService replicaService;

    @Autowired
    ReplicaJpaRepository replicaJpaRepository;

    @Test
    void findById() {
        //given
        Replica replica = givenReplica("TR16 308SR", "example@gmail.com");

        //when
        Replica foundReplica = basicReplicaService.findOneById(replica.getId()).get();

        //then
        assertEquals(replica.getId(), foundReplica.getId());
        assertEquals("TR16 308SR", foundReplica.getName());
    }

    @Test
    void updateReplica() {
        //given
        Replica replica = givenReplica("TR16 308SR", "example@gmail.com");
        String newAdditionalEquipment = "5 mags";
        ReplicaUseCase.UpdateReplicaCommand updateCommand =
                new ReplicaUseCase.UpdateReplicaCommand(replica.getId(), null, null, newAdditionalEquipment);
        //when
        replicaService.updateReplica(updateCommand);

        //then
        assertEquals(newAdditionalEquipment, replicaJpaRepository.findById(replica.getId()).get().getAdditionalEquipment());
        assertEquals("TR16 308SR", replicaJpaRepository.findById(replica.getId()).get().getName());

    }

    private Replica givenReplica(String replicaName, String ownerEmail){
        BasicReplicaUseCase.CreateOwnerCommand ownerCommand = toCreateOwnerCommand(ownerEmail);
        BasicReplicaUseCase.CreateReplicaCommand command = new BasicReplicaUseCase.CreateReplicaCommand(
                replicaName,
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand
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