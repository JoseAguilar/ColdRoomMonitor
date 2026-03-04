package com.joseag.coldroommonitor.application.command;

public record CreateSensorDeviceCommand(
        String name,
        Long coldRoomId,
        Boolean enabled
) {
}