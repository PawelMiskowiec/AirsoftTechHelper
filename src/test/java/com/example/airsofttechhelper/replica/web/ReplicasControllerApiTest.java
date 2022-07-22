package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.replica.domain.ReplicaStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class ReplicasControllerApiTest {

    @LocalServerPort
    private int port;

    @MockBean
    ReplicasController replicasController;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getAllReplicas() {
        //given
        RestMinReplica replica = new RestMinReplica(1L, "gg tr16", ReplicaStatus.TESTING, LocalDateTime.now(),
                "owner@gmail.com");
        RestMinReplica replica2 = new RestMinReplica(1L, "ea m4", ReplicaStatus.INPROGRESS, LocalDateTime.now(),
                "owner@gmail.com");

        Mockito.when(replicasController.getAllReplicas(Optional.empty())).thenReturn(List.of(replica, replica2));
        ParameterizedTypeReference<List<RestMinReplica>> typeReference = new ParameterizedTypeReference<>(){};

        //when
        String url = "http://localhost:" + port + "/replicas";
        RequestEntity<?> request = RequestEntity.get(URI.create(url)).build();
        List<RestMinReplica> response = restTemplate.exchange(request, typeReference).getBody();

        //then
        Assertions.assertEquals(2, response.size());
    }

    @Test
    void getReplicaById(){
        //given
        RestMinReplica replica = new RestMinReplica(1L, "gg tr16", ReplicaStatus.TESTING, LocalDateTime.now(),
                "owner@gmail.com");
        Mockito.when(replicasController.getReplicaById(1L)).thenReturn(ResponseEntity.ok(replica));

        //when
        String url = "http://localhost:" + port + "/replicas/1";
        RequestEntity<?> request = RequestEntity.get(url).build();
        ResponseEntity<RestMinReplica> response = restTemplate.exchange(request, RestMinReplica.class);

        //then
        Assertions.assertEquals(HttpStatus.OK.toString(), response.getStatusCode().toString());
        Assertions.assertEquals(replica.getName(), response.getBody().getName());
    }
}