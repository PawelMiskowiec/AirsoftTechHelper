package com.miskowiec.airsofttechhelper.part.application.port;

import com.miskowiec.airsofttechhelper.part.domain.Part;
import lombok.Value;

import java.util.List;
import java.util.Optional;

public interface PartUseCase {
    List<Part> findAll();
    Optional<Part> findById(Long id);
    Part addPart(CreatePartCommand command);
    void deletePart(Long id);

    @Value
    class CreatePartCommand{
        String name;
        String category;
    }

}
