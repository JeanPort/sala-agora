package com.jean.realmeet.unit;

import com.jean.realmeet.core.BaseUnitTest;
import com.jean.realmeet.domain.repository.RoomRepository;
import com.jean.realmeet.exception.ConflictException;
import com.jean.realmeet.exception.RoomNotFoundException;
import com.jean.realmeet.service.RoomService;
import com.jean.realmeet.utils.MapperUtils;
import com.jean.realmeet.utils.TestConstants;
import com.jean.realmeet.utils.TestDataCreator;
import com.jean.realmeet.validator.RoomValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;


public class RoomServiceUnitTest extends BaseUnitTest {

    @Mock
    private RoomRepository repository;
    @Mock
    private RoomValidator roomValidator;

    private RoomService victim;

    @BeforeEach
    void setUpEach(){
        victim = new RoomService(repository, MapperUtils.roomMapper(), roomValidator);
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
        Mockito.when(repository.findByIdAndActive(TestConstants.DEFAULT_ROOM_ID, true)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(RoomNotFoundException.class,() -> victim.getRoom(TestConstants.DEFAULT_ROOM_ID));
    }

    @Test
    void testCreateRoomSuccess(){
        var request = TestDataCreator.createRoomRequest();
        var roomExpected = TestDataCreator.roomBuilder().build();

        Mockito.when(repository.save(Mockito.any())).thenReturn(roomExpected);
        var result = victim.create(request);

        Assertions.assertEquals(roomExpected.getName(), result.name());
        Assertions.assertEquals(roomExpected.getSeats(), result.seats());
        verify(roomValidator).validateNameUniqueness(request.name(), null);
    }

    @Test
    void shouldThrowExceptionWhenValidatorFailsOnCreate() {
        var dto = TestDataCreator.createRoomRequest();

        doThrow(new ConflictException("Room name already in use"))
                .when(roomValidator)
                .validateNameUniqueness(dto.name(), null);

        assertThrows(ConflictException.class, () -> victim.create(dto));
    }

    @Test
    void shouldDeactivateRoomWhenRoomExistsAndIsActive(){
        var room = TestDataCreator.roomBuilder().id(TestConstants.DEFAULT_ROOM_ID).active(true).build();

        Mockito.when(repository.findByIdAndActive(room.getId(), true)).thenReturn(Optional.of(room));
        Assertions.assertDoesNotThrow(() -> victim.delete(room.getId()));
        Assertions.assertFalse(room.getActive());
        verify(repository).findByIdAndActive(room.getId(), true);
    }

    @Test
    void shouldThrowRoomNotFoundExceptionWhenRoomDoesNotExistOrIsInactive(){
        var roomId = TestConstants.DEFAULT_ROOM_ID;

        Mockito.when(repository.findByIdAndActive(roomId, true)).thenReturn(Optional.empty());

        Assertions.assertThrows(RoomNotFoundException.class, () -> victim.delete(roomId));
        verify(repository).findByIdAndActive(roomId, true);
    }
}
