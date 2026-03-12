package com.joseag.coldroommonitor.api.dto.request;


public record SensorDeviceUpdateRequest(
        String name,
        Long coldRoomId,
        Boolean enabled
) { }
