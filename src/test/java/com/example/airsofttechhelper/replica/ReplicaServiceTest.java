package com.example.airsofttechhelper.replica;

import com.example.airsofttechhelper.replica.application.ReplicaService;
import com.example.airsofttechhelper.replica.application.port.ReplicaUseCase;
import com.example.airsofttechhelper.replica.db.OwnerRepository;
import com.example.airsofttechhelper.replica.db.ReplicaRepository;
import com.example.airsofttechhelper.replica.domain.Owner;
import com.example.airsofttechhelper.replica.domain.Replica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static com.example.airsofttechhelper.replica.application.port.ReplicaUseCase.*;
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
