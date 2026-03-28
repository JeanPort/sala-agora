package com.jean.realmeet.unit;

import com.jean.realmeet.core.BaseUnitTest;
import com.jean.realmeet.mapper.RoomMapper;
import com.jean.realmeet.utils.MapperUtils;
import com.jean.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RoomMapperUnitTest extends BaseUnitTest {

    private RoomMapper victim;

    @BeforeEach
    void setUpEach(){
        victim = MapperUtils.roomMapper();
    }

    @Test
    void testFromEntityToResponse(){
        var room = TestDataCreator.roomBuilder().build();
        var dto = victim.fromRoomToResponse(room);

        Assertions.assertEquals(room.getId(), dto.id());
        Assertions.assertEquals(room.getName(), dto.name());
        Assertions.assertEquals(room.getSeats(), dto.seats());
    }
}
