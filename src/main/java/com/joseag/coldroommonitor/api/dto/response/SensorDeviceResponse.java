package com.joseag.coldroommonitor.api.dto.response;

public record SensorDeviceResponse(
        Long id,
        Long coldRoomId,
        String name,
        boolean enabled
) {}
