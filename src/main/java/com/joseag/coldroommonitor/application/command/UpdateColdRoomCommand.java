package com.joseag.coldroommonitor.application.command;

public record UpdateColdRoomCommand(
        Long id,
        String name,
        String location,
        Boolean enabled
) {
}
