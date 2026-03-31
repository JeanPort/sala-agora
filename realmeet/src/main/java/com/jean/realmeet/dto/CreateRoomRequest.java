package com.jean.realmeet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRoomRequest(
        @NotBlank String name,
        @NotNull Integer seats) {
}
