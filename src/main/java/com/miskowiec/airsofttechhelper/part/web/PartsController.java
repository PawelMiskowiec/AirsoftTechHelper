package com.miskowiec.airsofttechhelper.part.web;

import com.miskowiec.airsofttechhelper.part.application.port.PartUseCase;
import com.miskowiec.airsofttechhelper.part.application.port.PartUseCase.CreatePartCommand;
import com.miskowiec.airsofttechhelper.part.domain.Part;
import com.miskowiec.airsofttechhelper.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/parts")
@AllArgsConstructor
public class PartsController {

    private final PartUseCase service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RestPart> getAll(){
        return service.findAll()
                .stream()
                .map(this::toRestPart)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestPart> getById(@PathVariable Long id){
        Optional<Part> part = service.findById(id);
        if(part.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no part with id " + id);
        }
        return ResponseEntity.ok(toRestPart(part.get()));
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<Object> addPart(@RequestBody @Validated CreatePartRestCommand command){
        Part part= service.addPart(command.toCreateCommand());
        URI uri = new CreatedURI("/parts/" + part.getId().toString()).uri();
        return ResponseEntity.created(uri).build();
    }

    private RestPart toRestPart(Part part) {
        return new RestPart(part.getId(), part.getName(), part.getCategory().name());
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePart(@PathVariable Long id){
        service.deletePart(id);
    }

    @Value
    class CreatePartRestCommand{
        @NotBlank(message = "Please provide an parts name")
        String name;
        @NotBlank(message = "Please provide an parts category")
        String category;

        CreatePartCommand toCreateCommand(){
            return new CreatePartCommand(name, category);
        }
    }

}
