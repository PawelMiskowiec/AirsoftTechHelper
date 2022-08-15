package com.miskowiec.airsofttechhelper.part.web;

import com.miskowiec.airsofttechhelper.part.application.port.ReplicaPartUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReplicaPartsControllerIT {

    @Autowired
    ReplicaPartsController controller;

    @Autowired
    ReplicaPartUseCase replicaPartService;


}