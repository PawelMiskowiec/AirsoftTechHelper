package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.example.airsofttechhelper.part.web.ReplicaPartsController;
import com.example.airsofttechhelper.replica.application.port.BasicReplicaUseCase;
import com.example.airsofttechhelper.replica.domain.Replica;
import com.example.airsofttechhelper.replica.application.port.ToDoUseCase;
import com.example.airsofttechhelper.replica.web.dto.RestBasicReplica;
import com.example.airsofttechhelper.security.UserEntityDetails;
import com.example.airsofttechhelper.user.db.UserEntityRepository;
import com.example.airsofttechhelper.user.domain.UserEntity;
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
class BasicReplicasControllerIT {
    @Autowired
    BasicReplicaUseCase replicaService;

    @Autowired
    BasicReplicasController basicReplicasController;

    @Autowired
    ReplicaPartsController replicaPartsController;

    @Autowired
    ReplicaPartUseCase replicaPartService;

    @Autowired
    ToDoUseCase toDoService;

    @Autowired
    UserEntityRepository userEntityRepository;


    //Use of transactional resulted in non-deterministic test execution as the database had some additional replica
    //from method using dirtiesContext
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void  getAllReplicas(){
        //given
        UserDetails tech = givenUserDetails();
        Replica replica = givenReplica("GG tr16 308 sr", "pawel@miskowiec.com", tech);
        Replica replica2 = givenReplica("EA m4", "pawel@miskowiec.com", tech);

        //when
        List<RestBasicReplica> all = basicReplicasController.getAllUsersReplicas(Optional.empty(), tech);

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