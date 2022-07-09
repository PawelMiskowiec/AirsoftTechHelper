package com.example.airsofttechhelper.part.application;

import com.example.airsofttechhelper.part.application.port.PartUseCase;
import com.example.airsofttechhelper.part.db.PartRepository;
import com.example.airsofttechhelper.part.domain.Part;
import com.example.airsofttechhelper.part.domain.PartCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service

@Transactional
@AllArgsConstructor
public class PartService implements PartUseCase {
    private final PartRepository repository;

    @Override
    public List<Part> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Part> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Part addPart(CreatePartCommand command) {
        PartCategory partCategory = PartCategory.parseString(command.getCategory())
                                                .orElseThrow(() -> new IllegalArgumentException("Requested category doesn't exist"));
        Part part = new Part(command.getName(), partCategory);
        return repository.save(part);
    }

    @Override
    public void deletePart(Long id) {
        repository.deleteById(id);
    }
}
