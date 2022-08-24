package com.miskowiec.airsofttechhelper.part.application;

import com.miskowiec.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import com.miskowiec.airsofttechhelper.part.db.PartJpaRepository;
import com.miskowiec.airsofttechhelper.part.db.ReplicaPartJpaRepository;
import com.miskowiec.airsofttechhelper.part.domain.Part;
import com.miskowiec.airsofttechhelper.part.domain.PartCategory;
import com.miskowiec.airsofttechhelper.part.domain.ReplicaPart;
import com.miskowiec.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.security.UserEntityDetails;
import com.miskowiec.airsofttechhelper.user.db.UserEntityRepository;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static com.miskowiec.airsofttechhelper.part.application.port.ReplicaPartUseCase.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@Import(ReplicaPartService.class)
class ReplicaPartServiceIT {

    @Autowired
    ReplicaPartService replicaPartService;

    @Autowired
    ReplicaPartJpaRepository repository;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    ReplicaJpaRepository replicaJpaRepository;

    @Autowired
    PartJpaRepository partJpaRepository;

    @Test
    void findAllByUserDetails() {
        //given
        UserDetails user = givenAuthPrincipal("example@tech.com");
        UserDetails user2 = givenAuthPrincipal("example2@tech.com");
        Replica replica = givenReplica("tr16 test");
        Replica replica2 = givenReplica("tr16 test");
        givenReplicaPart("first", user, replica.getId());
        givenReplicaPart("second", user, replica.getId());
        givenReplicaPart("third", user2, replica2.getId());

        //when
        List<ReplicaPart> all = replicaPartService.findAllBy(user);

        //then
        assertEquals(2, all.size());

    }

    @Test
    void testFindAllByReplicaIdUserDetails() {
        //given
        UserDetails user = givenAuthPrincipal("example@tech.com");
        Replica replica = givenReplica("tr16 test");
        givenReplicaPart("first", user, replica.getId());
        givenReplicaPart("second", user, replica.getId());

        //when
        List<ReplicaPart> all = replicaPartService.findAllBy(replica.getId(), user);

        //then
        assertEquals(2, all.size());

    }

    @Test
    void addReplicaPart() {
        //given
        UserDetails userDetails = givenAuthPrincipal("example@tech.com");
        Replica replica = givenReplica("tr16 test");
        Part part = givenPart("testPart");
        UserEntity user = userEntityRepository.findByUsername(userDetails.getUsername()).get();
        CreateReplicaPartCommand command = new CreateReplicaPartCommand(
                replica.getId(),
                Optional.of(part.getId()),
                "test replica part",
                "MISC",
                "test notes",
                userDetails
        );

        //when
        ReplicaPart saved = replicaPartService.addReplicaPart(command);

        //then
        assertEquals(saved, repository.findById(saved.getId()).get());

    }

    @Test
    void updateNotes() {
        //given
        UserDetails user = givenAuthPrincipal("example@tech.com");
        Replica replica = givenReplica("tr16 test");
        ReplicaPart replicaPart = givenReplicaPart("pre update", user, replica.getId());
        UpdateReplicaPartNotesCommand command = new UpdateReplicaPartNotesCommand(replicaPart.getId(), "updated");

        //when
        replicaPartService.updateNotes(command);

        //then
        Assertions.assertEquals("updated", repository.findById(replicaPart.getId()).get().getNotes());

    }

    private Part givenPart(String name){
        return partJpaRepository.save(new Part(name, PartCategory.MISC));
    }
    private ReplicaPart givenReplicaPart(String notes, UserDetails userDetails, Long replicaId){
        Part part = new Part();
        Replica replica = replicaJpaRepository.getReferenceById(replicaId);
        UserEntity user = userEntityRepository.findByUsername(userDetails.getUsername()).get();
        return repository.save(new ReplicaPart(notes, part, replica, user));
    }

    private Replica givenReplica(String name){
        Replica replica = new Replica();
        replica.setName(name);
        return replicaJpaRepository.save(replica);
    }

    private UserDetails givenAuthPrincipal(String username){
        UserEntity userEntity = new UserEntity(username, "123");
        userEntityRepository.save(userEntity);
        return new UserEntityDetails(userEntity);
    }
}