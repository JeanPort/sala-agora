package com.jean.realmeet.validator;

import com.jean.realmeet.domain.repository.RoomRepository;
import com.jean.realmeet.exception.ConflictException;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {

    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validateNameUniqueness(String name, Long id){
        var roomExistent = (id == null)
                ? roomRepository.existsByNameAndActiveTrue(name)
                : roomRepository.existsByNameAndActiveTrueAndIdNot(name, id);

        if (roomExistent){
            throw new ConflictException("Room name already in use");
        }
    }
}
