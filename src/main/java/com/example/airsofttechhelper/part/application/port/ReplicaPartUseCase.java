package com.example.airsofttechhelper.part.application.port;

import com.example.airsofttechhelper.part.application.port.PartUseCase;
import com.example.airsofttechhelper.part.domain.ReplicaPart;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ReplicaPartUseCase {
    List<ReplicaPart> findAll();
    List<ReplicaPart> findAllByReplicaId(Long replicaId);
    Optional<ReplicaPart> findOneById(Long id);
    ReplicaPart addReplicaPart(CreateReplicaPartCommand command);
    UpdateNotesResponse updateNotes(Long id, String newNotes);

    @Value
    class UpdateNotesResponse{
        public static UpdateNotesResponse SUCCESS =
                new UpdateNotesResponse(true, Collections.emptyList());
        boolean success;
        List<String> errors;
    }

    @Value
    class CreateReplicaPartCommand{
        Long replicaId;
        Optional<Long> partId;
        String name;
        String category;
        String notes;

        public PartUseCase.CreatePartCommand toCreatePartCommand(String partName, String partCategory) {
            return new PartUseCase.CreatePartCommand(partName, partCategory);
        }
    }
}
