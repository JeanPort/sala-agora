package com.jean.realmeet.integration;

import com.jean.realmeet.core.BaseIntegrationTest;
import com.jean.realmeet.domain.entity.Room;
import com.jean.realmeet.domain.repository.RoomRepository;
import com.jean.realmeet.utils.TestConstants;
import com.jean.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class RoomApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void getRoomSuccess() throws Exception {
        // 🔹 Arrange (criar dados no banco)
        Room room = TestDataCreator.roomBuilder().build();

        Room savedRoom = roomRepository.save(room);

        // 🔹 Act + Assert
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
        // 🔹 Arrange (criar dados no banco)

        Room room = TestDataCreator.roomBuilder().active(false).build();
        Room savedRoom = roomRepository.save(room);

        Assertions.assertEquals(false, savedRoom.getActive());

        // 🔹 Act + Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{id}", savedRoom.getId()))
                .andExpect(status().isNotFound());
    }
}
