package com.miskowiec.airsofttechhelper.replica.web;

import com.miskowiec.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.replica.web.dto.RestBasicReplica;
import com.miskowiec.airsofttechhelper.security.UserEntityDetails;
import com.miskowiec.airsofttechhelper.user.db.UserEntityJpaRepository;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@WithMockUser
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReplicasControllerIT {
    @Autowired
    BasicReplicaUseCase replicaService;
    @Autowired
    ReplicasController replicasController;
    @Autowired
    UserEntityJpaRepository userEntityJpaRepository;


    //Use of transactional resulted in non-deterministic test execution as the database had some additional replica
    //from method using dirtiesContext
    @Test
    public void  getAllReplicas(){
        //given
        UserDetails tech = givenUserDetails();
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com", tech);
        Replica replica2 = givenReplica("EA m4", "pawel@miskowiec.com", tech);

        //when
        List<RestBasicReplica> all = replicasController.getAllUsersReplicas(Optional.empty(), tech);

        //then
        Assertions.assertEquals(2, all.size());
    }


    private Replica givenReplica(String replicaName, String ownerEmail, UserDetails tech){
        BasicReplicaUseCase.CreateOwnerCommand ownerCommand = toCreateOwnerCommand(ownerEmail);
        BasicReplicaUseCase.CreateReplicaCommand command = new BasicReplicaUseCase.CreateReplicaCommand(
                replicaName,
                "this replica is supposed to be fully upgraded",
                "3 mid-cap magazines",
                ownerCommand,
                tech
        );
        return replicaService.addReplica(command);
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