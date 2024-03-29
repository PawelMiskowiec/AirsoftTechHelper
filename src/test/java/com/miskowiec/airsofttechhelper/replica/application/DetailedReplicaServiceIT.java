package com.miskowiec.airsofttechhelper.replica.application;

import com.miskowiec.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.miskowiec.airsofttechhelper.replica.application.port.DetailedReplicaUseCase;
import com.miskowiec.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.security.UserEntityDetails;
import com.miskowiec.airsofttechhelper.user.db.UserEntityJpaRepository;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DetailedReplicaServiceIT {

    @Autowired
    BasicReplicaService basicReplicaService;

    @Autowired
    DetailedReplicaService detailedReplicaService;

    @Autowired
    ReplicaJpaRepository replicaJpaRepository;

    @Autowired
    UserEntityJpaRepository userEntityJpaRepository;

    @Test
    void findById() {
        //given
        Replica replica = givenReplica("TR16 308SR", "example@gmail.com", givenUserDetails());

        //when
        Replica foundReplica = detailedReplicaService.findById(replica.getId()).get();

        //then
        assertEquals(replica.getId(), foundReplica.getId());
        assertEquals("TR16 308SR", foundReplica.getName());
    }

    @Test
    void updateReplica() {
        //given
        UserDetails userDetails = givenUserDetails();
        Replica replica = givenReplica("TR16 308SR", "example@gmail.com", userDetails);
        String newName = "TR16 308WH";
        String newDescription = "ReplicaOwner has paid extra to be done quickly";
        String newAdditionalEquipment = "5 mags";
        DetailedReplicaUseCase.UpdateReplicaCommand updateCommand =
                new DetailedReplicaUseCase.UpdateReplicaCommand(replica.getId(), newName, newDescription, newAdditionalEquipment, userDetails);
        //when
        detailedReplicaService.updateReplica(updateCommand);

        //then
        assertEquals(newName, replicaJpaRepository.findById(replica.getId()).get().getName());
        assertEquals(newDescription, replicaJpaRepository.findById(replica.getId()).get().getDescription());
        assertEquals(newAdditionalEquipment, replicaJpaRepository.findById(replica.getId()).get().getAdditionalEquipment());
    }

    @Test
    void updateOnlyFewFieldsOfReplica() {
        //given
        UserDetails userDetails = givenUserDetails();
        Replica replica = givenReplica("TR16 308SR", "example@gmail.com", userDetails);
        String newAdditionalEquipment = "5 mags";
        DetailedReplicaUseCase.UpdateReplicaCommand updateCommand =
                new DetailedReplicaUseCase.UpdateReplicaCommand(replica.getId(), null, null, newAdditionalEquipment, userDetails);
        //when
        detailedReplicaService.updateReplica(updateCommand);

        //then
        assertEquals(newAdditionalEquipment, replicaJpaRepository.findById(replica.getId()).get().getAdditionalEquipment());
        assertEquals("TR16 308SR", replicaJpaRepository.findById(replica.getId()).get().getName());
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

    private UserDetails givenUserDetails() {
        UserEntity userEntity = new UserEntity("example@tech.com", "123");
        userEntityJpaRepository.save(userEntity);
        return new UserEntityDetails(userEntity);
    }

    private BasicReplicaUseCase.CreateOwnerCommand toCreateOwnerCommand(String email) {
        return new BasicReplicaUseCase.CreateOwnerCommand(
                "Pawel", "7123123", "example 12/3",
                "City", "11-123", email
        );
    }
}