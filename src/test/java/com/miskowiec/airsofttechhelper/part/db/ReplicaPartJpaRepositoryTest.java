package com.miskowiec.airsofttechhelper.part.db;

import com.miskowiec.airsofttechhelper.part.domain.Part;
import com.miskowiec.airsofttechhelper.part.domain.ReplicaPart;
import com.miskowiec.airsofttechhelper.replica.db.ReplicaJpaRepository;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.user.db.UserEntityJpaRepository;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@AutoConfigureTestDatabase
@DataJpaTest
class ReplicaPartJpaRepositoryTest {

    @Autowired
    ReplicaPartJpaRepository repository;

    @Autowired
    UserEntityJpaRepository userEntityJpaRepository;

    @Autowired
    ReplicaJpaRepository replicaJpaRepository;

    @Test
    public void findAllByUserName(){
        //given
        UserEntity user = givenUserEntity("test@user.com");
        UserEntity otherUser = givenUserEntity("test2@user.com");
        Replica replica = givenReplica();
        Replica replica2 = givenReplica();
        givenReplicaPart("part1", "notes1", user, replica);
        givenReplicaPart("part2", "notes2", user, replica);
        givenReplicaPart("part3", "notes3", otherUser, replica2);

        //when
        List<ReplicaPart> all = repository.findAllByUsername(user.getUsername());

        //then
        Assertions.assertEquals(2, all.size());

    }

    @Test
    public void findAllByReplicaIdAndUsername(){
        //given
        UserEntity user = givenUserEntity("test@user.com");
        UserEntity otherUser = givenUserEntity("test2@user.com");
        Replica replica = givenReplica();
        Replica replica2 = givenReplica();
        Replica replica3 = givenReplica();
        givenReplicaPart("part1", "notes1", user, replica);
        givenReplicaPart("part2", "notes2", user, replica);
        givenReplicaPart("part3", "notes3", user, replica2);
        givenReplicaPart("part4", "notes4", otherUser, replica3);

        //when
        List<ReplicaPart> all = repository.findAllByReplicaIdAndUsername(replica.getId(), user.getUsername());

        //then
        Assertions.assertEquals(2, all.size());

    }

    private UserEntity givenUserEntity(String name) {
        return userEntityJpaRepository.save(new UserEntity(name, "test123"));
    }

    private Replica givenReplica(){
        return replicaJpaRepository.save(new Replica());
    }

    private ReplicaPart givenReplicaPart(String name, String notes, UserEntity user, Replica replica){
        Part part = new Part();
        part.setName(name);
        ReplicaPart replicaPart = new ReplicaPart();
        replicaPart.setReplica(replica);
        replicaPart.setPart(part);
        replicaPart.setNotes(notes);
        replicaPart.setUser(user);
        return repository.save(replicaPart);
    }

}