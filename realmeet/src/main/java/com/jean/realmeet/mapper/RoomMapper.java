package com.jean.realmeet.mapper;

import com.jean.realmeet.domain.entity.Room;
import com.jean.realmeet.dto.CreateRoomRequest;
import com.jean.realmeet.dto.RoomResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room fromCreateRoomRequestToEntity(CreateRoomRequest request);
    RoomResponse fromRoomToResponse(Room room);
}
