package com.miskowiec.airsofttechhelper.replica.web;

import com.miskowiec.airsofttechhelper.replica.application.BasicReplicaService;
import com.miskowiec.airsofttechhelper.replica.domain.Replica;
import com.miskowiec.airsofttechhelper.replica.domain.ReplicaOwner;
import com.miskowiec.airsofttechhelper.replica.domain.ReplicaStatus;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTesUserConfigForControllersTests.class
)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class ReplicasControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BasicReplicaService basicReplicaService;
    @Autowired
    UserDetails testUser;

    @Test
    @WithUserDetails("example@tech.com")
    void getAllReplicas() throws Exception {
        //given
        Replica replica = givenReplica("gg tr16", null);
        Replica replica2 = givenReplica("ea m4", null);
        List<Replica> replicas = List.of(replica, replica2);

        //when
        Mockito.when(basicReplicaService.findAllUserReplicas(testUser.getUsername())).thenReturn(replicas);

        //then
        mockMvc.perform(get("/replicas"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "[" +
                                "{\"id\":null,\"name\":\"gg tr16\",\"status\":\"NEW\",\"createdAt\":null,\"ownerEmail\":\"owner@gmail.com\"}," +
                                "{\"id\":null,\"name\":\"ea m4\",\"status\":\"NEW\",\"createdAt\":null,\"ownerEmail\":\"owner@gmail.com\"}" +
                                "]"
                ));
    }

    @Test
    @WithUserDetails("example@tech.com")
    void getReplicaById() throws Exception {
        Replica replica = givenReplica("gg tr16", 1l);
        Mockito.when(basicReplicaService.findOneById(1L)).thenReturn(Optional.of(replica));
        mockMvc.perform(get("/replicas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("" +
                        "{\"id\":1,\"name\":\"gg tr16\",\"status\":\"NEW\",\"createdAt\":null,\"ownerEmail\":\"owner@gmail.com\"}"
                ));
    }

    private Replica givenReplica(String name, Long id){
        ReplicaOwner owner = getReplicaOwner();
        Replica replica = Replica.builder()
                .name(name)
                .replicaOwner(owner)
                .status(ReplicaStatus.NEW)
                .user(givenUserEntity())
                .build();
        replica.setId(id);
        return replica;
    }

    private static UserEntity givenUserEntity() {
        return new UserEntity("example@tech.com", "pass123");
    }

    private ReplicaOwner getReplicaOwner() {
        ReplicaOwner owner = new ReplicaOwner();
        owner.setEmail("owner@gmail.com");
        return owner;
    }

}