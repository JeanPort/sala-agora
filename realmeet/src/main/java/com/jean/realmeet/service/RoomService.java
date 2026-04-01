package com.jean.realmeet.service;

import com.jean.realmeet.domain.entity.Room;
import com.jean.realmeet.domain.repository.RoomRepository;
import com.jean.realmeet.dto.CreateRoomRequest;
import com.jean.realmeet.dto.RoomResponse;
import com.jean.realmeet.exception.ConflictException;
import com.jean.realmeet.exception.RoomNotFoundException;
import com.jean.realmeet.mapper.RoomMapper;
import com.jean.realmeet.validator.RoomValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.nonNull;

@Service
@Transactional
public class RoomService {

    private final RoomRepository repository;
    private final RoomMapper mapper;
    private final RoomValidator validator;

    public RoomService(RoomRepository repository, RoomMapper mapper, RoomValidator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoom(Long roomId){
        nonNull(roomId);
        return repository.findByIdAndActive(roomId, true)
                .map(mapper::fromRoomToResponse)
                .orElseThrow(RoomNotFoundException::new);
    }

    @Transactional
    public RoomResponse create(CreateRoomRequest request){
        validator.validateNameUniqueness(request.name(), null);
        var room = mapper.fromCreateRoomRequestToEntity(request);
        repository.save(room);
        return mapper.fromRoomToResponse(room);
    }

    @Transactional
    public void delete(Long id){
        var room = repository.findByIdAndActive(id, true)
                .orElseThrow(RoomNotFoundException::new);

        room.desative();
    }
}
