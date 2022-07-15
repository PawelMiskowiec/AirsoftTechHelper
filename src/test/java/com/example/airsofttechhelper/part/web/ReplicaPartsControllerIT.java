package com.example.airsofttechhelper.part.web;

import com.example.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReplicaPartsControllerIT {

    @Autowired
    ReplicaPartsController controller;

    @Autowired
    ReplicaPartUseCase replicaPartService;


}