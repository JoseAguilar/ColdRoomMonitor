package com.joseag.coldroommonitor.api.mappers;

import com.joseag.coldroommonitor.api.dto.request.ColdRoomCreateRequest;
import com.joseag.coldroommonitor.api.dto.response.ColdRoomResponse;
import com.joseag.coldroommonitor.application.command.CreateColdRoomCommand;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ColdRoomMapper {

    public ColdRoomResponse toResponse(ColdRoom coldRoom){
        return new ColdRoomResponse(
                coldRoom.getId(),
                coldRoom.getName(),
                coldRoom.getLocation(),
                coldRoom.isEnabled()
        );
    }

    public ColdRoom fromCreateCommand(CreateColdRoomCommand command){
        ColdRoom coldRoom = new ColdRoom(command.name(), command.location());
        coldRoom.setEnabled(command.enabled());
        return coldRoom;
    }


    public Page<ColdRoomResponse> toResponsePage(Page<ColdRoom> coldRooms) {
        return coldRooms.map(this::toResponse);
    }
}
