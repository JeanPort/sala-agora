package com.jean.realmeet.unit;

import com.jean.realmeet.core.BaseUnitTest;
import com.jean.realmeet.domain.repository.RoomRepository;
import com.jean.realmeet.exception.RoomNotFoundException;
import com.jean.realmeet.service.RoomService;
import com.jean.realmeet.utils.MapperUtils;
import com.jean.realmeet.utils.TestConstants;
import com.jean.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;


public class RoomServiceUnitTest extends BaseUnitTest {

    @Mock
    private RoomRepository repository;

    private RoomService victim;

    @BeforeEach
    void setUpEach(){
        victim = new RoomService(repository, MapperUtils.roomMapper());
    }

    @Test
    void testGetRoomSuccess(){

        var roomExpected = TestDataCreator.roomBuilder().build();
        Mockito.when(repository.findByIdAndActive(TestConstants.DEFAULT_ROOM_ID, true)).thenReturn(Optional.of(roomExpected));

        var result = victim.getRoom(TestConstants.DEFAULT_ROOM_ID);

        Assertions.assertEquals(roomExpected.getId(), result.id());
        Assertions.assertEquals(roomExpected.getName(), result.name());
        Assertions.assertEquals(roomExpected.getSeats(), result.seats());
    }

    @Test
    void testGetRoomNotFound(){

        var room = TestDataCreator.roomBuilder().build();
        Mockito.when(repository.findByIdAndActive(TestConstants.DEFAULT_ROOM_ID, true)).thenReturn(Optional.empty());

        Assertions.assertThrowsExactly(RoomNotFoundException.class,() -> victim.getRoom(TestConstants.DEFAULT_ROOM_ID));

    }
}
