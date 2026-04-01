package com.jean.realmeet.service;

import com.jean.realmeet.domain.entity.Room;
import com.jean.realmeet.domain.repository.RoomRepository;
import com.jean.realmeet.dto.CreateRoomRequest;
import com.jean.realmeet.dto.RoomResponse;
import com.jean.realmeet.dto.UpdateRoomRequest;
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
        var room = getActiveRoomOrThrow(roomId);
        return mapper.fromRoomToResponse(room);
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
        getActiveRoomOrThrow(id);
        repository.deactive(id);
    }

    @Transactional
    public RoomResponse updateRoom(Long rooId, UpdateRoomRequest request){
        validator.validateNameUniqueness(request.name(), rooId);
        var room = getActiveRoomOrThrow(rooId);
        room.update(request.name(), request.seats());
        return mapper.fromRoomToResponse(repository.save(room));
    }

    private Room getActiveRoomOrThrow(Long roomId) {
        nonNull(roomId);
        return repository.findByIdAndActive(roomId, true)
                .orElseThrow(RoomNotFoundException::new);
    }


}
