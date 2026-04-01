package com.jean.realmeet.utils;

import com.jean.realmeet.domain.entity.Room;
import com.jean.realmeet.dto.CreateRoomRequest;
import com.jean.realmeet.dto.UpdateRoomRequest;

public final class TestDataCreator {

    private TestDataCreator(){}

    public static Room.RoomBuilder roomBuilder(){
        return Room.builder()
                .name(TestConstants.DEFAULT_ROOM_NAME)
                .seats(TestConstants.DEFAULT_ROOM_SEATS);
    }

    public static CreateRoomRequest createRoomRequest(){
        return new CreateRoomRequest(TestConstants.DEFAULT_ROOM_NAME, TestConstants.DEFAULT_ROOM_SEATS);
    }

    public static UpdateRoomRequest updateRoomRequest(){
        return new UpdateRoomRequest(TestConstants.DEFAULT_ROOM_NAME, TestConstants.DEFAULT_ROOM_SEATS);
    }
}
