package com.joseag.coldroommonitor.api.dto.response;

public record ColdRoomResponse(
        Long id,
        String name,
        String location,
        boolean enabled
) {}
