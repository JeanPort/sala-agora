package com.jean.realmeet.unit;


import com.jean.realmeet.core.BaseUnitTest;
import com.jean.realmeet.domain.repository.RoomRepository;
import com.jean.realmeet.exception.ConflictException;
import com.jean.realmeet.utils.TestConstants;
import com.jean.realmeet.validator.RoomValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoomValidatorUnitTest extends BaseUnitTest {

    @Mock
    private RoomRepository roomRepository;

    private RoomValidator victim;

    @BeforeEach
    void setUpEach(){
        victim = new RoomValidator(roomRepository);
    }

    @Test
    void shouldNotThrowExceptionWhenNameIsAvailableOnCreate() {
        var name = TestConstants.DEFAULT_ROOM_NAME;
        when(roomRepository.existsByNameAndActiveTrue(name))
                .thenReturn(false);
        assertDoesNotThrow(() ->
                victim.validateNameUniqueness(name, null)
        );
        verify(roomRepository).existsByNameAndActiveTrue(name);
    }

    @Test
    void shouldNotThrowExceptionWhenUpdatingSameRoom() {
        String name = TestConstants.DEFAULT_ROOM_NAME;
        Long id = TestConstants.DEFAULT_ROOM_ID;
        when(roomRepository.existsByNameAndActiveTrueAndIdNot(name, id))
                .thenReturn(false);
        assertDoesNotThrow(() ->
                victim.validateNameUniqueness(name, id)
        );
        verify(roomRepository).existsByNameAndActiveTrueAndIdNot(name, id);
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExistsOnCreate() {
        String name = TestConstants.DEFAULT_ROOM_NAME;
        when(roomRepository.existsByNameAndActiveTrue(name))
                .thenReturn(true);
        assertThrows(ConflictException.class, () ->
                victim.validateNameUniqueness(name, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExistsOnUpdate() {
        String name = TestConstants.DEFAULT_ROOM_NAME;
        Long id = TestConstants.DEFAULT_ROOM_ID;
        when(roomRepository.existsByNameAndActiveTrueAndIdNot(name, id))
                .thenReturn(true);
        assertThrows(ConflictException.class, () ->
                victim.validateNameUniqueness(name, id)
        );
    }
}
