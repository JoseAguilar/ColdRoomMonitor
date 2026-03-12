package com.joseag.coldroommonitor.api.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ColdRoomCreateRequest(
        @NotBlank
        String name,

        @NotBlank
        String location,

        @NotNull
        Boolean enabled
) { }
