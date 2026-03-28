package com.jean.realmeet.mapper;

import com.jean.realmeet.domain.entity.Room;
import com.jean.realmeet.dto.RoomRequest;
import com.jean.realmeet.dto.RoomResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room fromRoomRequestToEntity(RoomRequest request);
    RoomResponse fromRoomToResponse(Room room);
}
