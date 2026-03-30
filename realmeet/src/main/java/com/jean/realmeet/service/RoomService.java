package com.jean.realmeet.service;

import com.jean.realmeet.domain.repository.RoomRepository;
import com.jean.realmeet.dto.RoomResponse;
import com.jean.realmeet.exception.RoomNotFoundException;
import com.jean.realmeet.mapper.RoomMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class RoomService {

    private final RoomRepository repository;
    private final RoomMapper mapper;

    public RoomService(RoomRepository repository, RoomMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoom(Long roomId){
        Objects.nonNull(roomId);
        return repository.findByIdAndActive(roomId, true)
                .map(mapper::fromRoomToResponse)
                .orElseThrow(RoomNotFoundException::new);
    }
}
