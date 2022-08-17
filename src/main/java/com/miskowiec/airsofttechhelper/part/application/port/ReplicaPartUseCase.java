package com.miskowiec.airsofttechhelper.part.application.port;

import com.miskowiec.airsofttechhelper.part.domain.ReplicaPart;
import lombok.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ReplicaPartUseCase {
    List<ReplicaPart> findAllBy(UserDetails user);
    List<ReplicaPart> findAllBy(Long replicaId, UserDetails user);
    ReplicaPart addReplicaPart(CreateReplicaPartCommand command);
    UpdateNotesResponse updateNotes(UpdateReplicaPartNotesCommand command);

    @Value
    class CreateReplicaPartCommand{
        Long replicaId;
        Optional<Long> partId;
        String name;
        String category;
        String notes;
        UserDetails user;

        public PartUseCase.CreatePartCommand toCreatePartCommand(String partName, String partCategory) {
            return new PartUseCase.CreatePartCommand(partName, partCategory);
        }
    }

    @Value
    class UpdateReplicaPartNotesCommand{
        Long replicaPartId;
        String notes;
    }

    @Value
    class UpdateNotesResponse{
        public static UpdateNotesResponse SUCCESS =
                new UpdateNotesResponse(true, Collections.emptyList());
        boolean success;
        List<String> errors;
    }

}
