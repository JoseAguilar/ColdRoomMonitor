package com.joseag.coldroommonitor.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SensorDeviceCreateRequest (
        @NotBlank
        String name,

        @NotNull
        Long coldRoomId,

        @NotNull
        Boolean enabled
){ }
