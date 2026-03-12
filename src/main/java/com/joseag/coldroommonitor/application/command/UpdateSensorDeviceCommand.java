package com.joseag.coldroommonitor.application.command;

public record UpdateSensorDeviceCommand(
        Long id,
        String name,
        Long coldRoomId,
        Boolean enabled
) {
}