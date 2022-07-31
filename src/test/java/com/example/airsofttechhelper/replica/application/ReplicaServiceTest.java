package com.example.airsofttechhelper.replica.application;

import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.example.airsofttechhelper.replica.application.port.DetailedReplicaUseCase;
import com.example.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.security.UserEntityDetails;
import com.example.airsofttechhelper.user.db.UserEntityRepository;
import com.example.airsofttechhelper.user.domain.UserEntity;
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
class ReplicaServiceTest {

    @Autowired
    BasicBasicReplicaService basicReplicaService;

    @Autowired
    DetailedReplicaService replicaService;

    @Autowired
    ReplicaJpaRepository replicaJpaRepository;

    @Autowired
    UserEntityRepository userEntityRepository;

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
        String newName = "TR16 308WH";
        String newDescription = "ReplicaOwner has paid extra to be done quickly";
        String newAdditionalEquipment = "5 mags";
        DetailedReplicaUseCase.UpdateReplicaCommand updateCommand =
                new DetailedReplicaUseCase.UpdateReplicaCommand(replica.getId(), newName, newDescription, newAdditionalEquipment);
        //when
        replicaService.updateReplica(updateCommand);

        //then
        assertEquals(newName, replicaJpaRepository.findById(replica.getId()).get().getName());
        assertEquals(newDescription, replicaJpaRepository.findById(replica.getId()).get().getDescription());
        assertEquals(newAdditionalEquipment, replicaJpaRepository.findById(replica.getId()).get().getAdditionalEquipment());


    }

    @Test
    void updateOnlyFewFieldsOfReplica() {
        //given
        Replica replica = givenReplica("TR16 308SR", "example@gmail.com");
        String newAdditionalEquipment = "5 mags";
        DetailedReplicaUseCase.UpdateReplicaCommand updateCommand =
                new DetailedReplicaUseCase.UpdateReplicaCommand(replica.getId(), null, null, newAdditionalEquipment);
        //when
        replicaService.updateReplica(updateCommand);

        //then
        assertEquals(newAdditionalEquipment, replicaJpaRepository.findById(replica.getId()).get().getAdditionalEquipment());
        assertEquals("TR16 308SR", replicaJpaRepository.findById(replica.getId()).get().getName());

    }

    private Replica givenReplica(String replicaName, String ownerEmail) {
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