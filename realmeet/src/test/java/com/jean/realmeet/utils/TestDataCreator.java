package com.jean.realmeet.utils;

import com.jean.realmeet.domain.entity.Room;

public final class TestDataCreator {

    private TestDataCreator(){}

    public static Room.RoomBuilder roomBuilder(){
        return Room.builder()
                .name(TestConstants.DEFAULT_ROOM_NAME)
                .seats(TestConstants.DEFAULT_ROOM_SEATS);
    }
}
