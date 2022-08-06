package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import com.example.airsofttechhelper.replica.web.dto.RestBasicReplica;
import com.example.airsofttechhelper.security.UserEntityDetails;
import com.example.airsofttechhelper.user.db.UserEntityRepository;
import com.example.airsofttechhelper.user.domain.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BasicReplicasControllerApiTest {

    @LocalServerPort
    private int port;

    @MockBean
    BasicReplicasController basicReplicasController;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Test
    void getAllReplicas() {
        //given
        UserDetails userDetails = givenUserDetails();
        RestBasicReplica replica = new RestBasicReplica(1L, "gg tr16", ReplicaStatus.TESTING, LocalDateTime.now(),
                "replicaOwner@gmail.com");
        RestBasicReplica replica2 = new RestBasicReplica(1L, "ea m4", ReplicaStatus.INPROGRESS, LocalDateTime.now(),
                "replicaOwner@gmail.com");

        Mockito.when(basicReplicasController.getAllUsersReplicas(Optional.empty(), userDetails)).thenReturn(List.of(replica, replica2));
        ParameterizedTypeReference<List<RestBasicReplica>> typeReference = new ParameterizedTypeReference<>(){};

        //when
        String url = "http://localhost:" + port + "/replicas-list";
        RequestEntity<?> request = RequestEntity.get(URI.create(url)).build();
        List<RestBasicReplica> response = restTemplate.exchange(request, typeReference).getBody();

        //then
        Assertions.assertEquals(2, response.size());
    }

    @Test
    void getReplicaById(){
        //given
        RestBasicReplica replica = new RestBasicReplica(1L, "gg tr16", ReplicaStatus.TESTING, LocalDateTime.now(),
                "replicaOwner@gmail.com");
        UserDetails userDetails = givenUserDetails();
        Mockito.when(basicReplicasController.getReplicaById(1L, userDetails)).thenReturn(ResponseEntity.ok(replica));

        //when
        String url = "http://localhost:" + port + "/replicas-list/1";
        RequestEntity<?> request = RequestEntity.get(url).build();
        ResponseEntity<RestBasicReplica> response = restTemplate.exchange(request, RestBasicReplica.class);

        //then
        Assertions.assertEquals(HttpStatus.OK.toString(), response.getStatusCode().toString());
        Assertions.assertEquals(replica.getName(), response.getBody().getName());
    }

    private UserDetails givenUserDetails() {
        UserEntity userEntity = new UserEntity("example@tech.com", "123");
        userEntityRepository.save(userEntity);
        return new UserEntityDetails(userEntity);
    }
}