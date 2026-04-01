package com.jean.realmeet.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jean.realmeet.core.BaseIntegrationTest;
import com.jean.realmeet.domain.entity.Room;
import com.jean.realmeet.domain.repository.RoomRepository;
import com.jean.realmeet.dto.CreateRoomRequest;
import com.jean.realmeet.dto.UpdateRoomRequest;
import com.jean.realmeet.utils.TestConstants;
import com.jean.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class RoomApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void getRoomSuccess() throws Exception {
        Room room = TestDataCreator.roomBuilder().build();

        Room savedRoom = roomRepository.save(room);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{id}", savedRoom.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRoom.getId()))
                .andExpect(jsonPath("$.name").value(savedRoom.getName()))
                .andExpect(jsonPath("$.seats").value(savedRoom.getSeats()))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getRoomDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{id}", TestConstants.DEFAULT_ROOM_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRoomActive() throws Exception {
        Room room = TestDataCreator.roomBuilder().active(false).build();
        Room savedRoom = roomRepository.save(room);

        Assertions.assertEquals(false, savedRoom.getActive());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{id}", savedRoom.getId()))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Should return 201 and created room when request is valid")
    void createRoom_whenRequestIsValid_shouldReturnCreatedRoom() throws Exception {
        var request = TestDataCreator.createRoomRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.seats").value(request.seats()))
                .andExpect(jsonPath("$.active").value(true));
    }


    @Test
    @DisplayName("Should return 422 when room fields are null")
    void createRoom_whenFieldsAreNull_shouldReturnUnprocessableEntity() throws Exception {
        var request = new CreateRoomRequest(null, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("Should return 409 when creating room with duplicate active name")
    void createRoom_whenNameIsDuplicate_shouldReturnConflict() throws Exception {
        var roomCreated = roomRepository.saveAndFlush(TestDataCreator.roomBuilder().build());

        Assertions.assertNotNull(roomCreated.getId());
        Assertions.assertEquals(1, roomRepository.count());

        var request = new CreateRoomRequest(roomCreated.getName(), roomCreated.getSeats());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteRoomShouldSoftDeleteSuccessfully() throws Exception {
        Long roomId = roomRepository.saveAndFlush(TestDataCreator.roomBuilder().active(true).build()).getId();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/rooms/{id}", roomId))
                .andExpect(status().isNoContent());

        var deletedRoom = roomRepository.findById(roomId);

        Assertions.assertTrue(deletedRoom.isPresent());
        Assertions.assertFalse(deletedRoom.get().getActive());
    }

    @Test
    void deleteRoomShouldReturnNotFoundWhenRoomDoesNotExist() throws Exception {
        var nonExistentId = TestConstants.DEFAULT_ROOM_ID;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/rooms/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRoomShouldReturnUpdatedRoomWhenValidRequest() throws Exception {
        var room = TestDataCreator.roomBuilder().build();

        var idRoomSaved = roomRepository.saveAndFlush(room).getId();
        var request = new UpdateRoomRequest("Novo nome", 1);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/rooms/{id}", idRoomSaved)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.seats").value(request.seats()))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void updateRoomShouldReturnNotFoundWhenRoomDoesNotExist() throws Exception {
        var request = TestDataCreator.updateRoomRequest();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/rooms/{id}", TestConstants.DEFAULT_ROOM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    @Test
    void updateRoomShouldReturnNotFoundWhenRoomIsInactive() throws Exception {
        var room = TestDataCreator.roomBuilder().active(false).build();
        var idRoomSaved = roomRepository.saveAndFlush(room).getId();
        var request = new UpdateRoomRequest("Novo nome", 1);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/rooms/{id}", idRoomSaved)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRoomShouldReturnConflictWhenNameAlreadyExists() throws Exception {
        var room1 = roomRepository.save(
                TestDataCreator.roomBuilder()
                        .name("Sala A")
                        .active(true)
                        .build()
        );
        var room2 = roomRepository.save(
                TestDataCreator.roomBuilder()
                        .name("Sala B")
                        .active(true)
                        .build()
        );

        var request = new UpdateRoomRequest(room1.getName(), 10);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/rooms/{id}", room2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateRoomShouldReturnBadRequestWhenNameIsBlank (){}

    @Test
    void updateRoomShouldReturnBadRequestWhenNameIsNull(){}

    @Test
    void updateRoomShouldReturnBadRequestWhenSeatsIsNull(){}

}
