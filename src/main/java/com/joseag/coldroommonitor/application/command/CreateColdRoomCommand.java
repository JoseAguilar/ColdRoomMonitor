package com.joseag.coldroommonitor.application.command;

public record CreateColdRoomCommand(
        String name,
        String location,
        Boolean enabled
) {
}