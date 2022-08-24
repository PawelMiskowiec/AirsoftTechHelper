package com.miskowiec.airsofttechhelper.part.application;

import com.miskowiec.airsofttechhelper.part.application.port.PartUseCase.CreatePartCommand;
import com.miskowiec.airsofttechhelper.part.db.PartJpaRepository;
import com.miskowiec.airsofttechhelper.part.domain.Part;
import com.miskowiec.airsofttechhelper.part.domain.PartCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PartServiceIT {

    @Autowired
    PartService partService;
    @Autowired
    PartJpaRepository repository;

    @Test
    public void findAll(){
        //given
        givenPart("First test part");
        Part secondTestPart = givenPart("Second test part");

        //when
        List<Part> all = partService.findAll();

        //then
        Assertions.assertEquals(2, all.size());
        Assertions.assertTrue(all.contains(secondTestPart));

    }

    @Test
    public void findById(){
        //given
        Part testPart = givenPart("First test part");

        //when
        Optional<Part> byId = partService.findById(testPart.getId());

        //then
        Assertions.assertEquals(testPart, byId.get());
    }

    @Test
    public void addPart(){
        //given
        CreatePartCommand command = new CreatePartCommand("Test part", "hopup");

        //when
        Part part = partService.addPart(command);

        //then
        Assertions.assertTrue(partService.findAll().contains(part));
    }

    @Test
    public void cannotAddPartWithInvalidCategory(){
        //given
        CreatePartCommand command = new CreatePartCommand("Test part", "invalid");

        //when
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> partService.addPart(command)
        );

        //then
        Assertions.assertTrue(exception.getMessage().contains("Requested category doesn't exist"));
    }

    @Test
    public void deletePart(){
        //given
        CreatePartCommand command = new CreatePartCommand("Test part", "hopup");

        //when
        Part part = partService.addPart(command);
        partService.deletePart(part.getId());

        //then
        Assertions.assertFalse(partService.findAll().contains(part));
    }

    private Part givenPart(String name) {
        Part part = new Part(name, PartCategory.MISC);
        return repository.save(part);
    }
}