package com.jean.realmeet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRoomRequest(
        @NotBlank String name,
        @NotNull Integer seats
) {
}
