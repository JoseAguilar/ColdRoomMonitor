package com.joseag.coldroommonitor.api.mappers;

import com.joseag.coldroommonitor.api.dto.request.ColdRoomCreateRequest;
import com.joseag.coldroommonitor.api.dto.response.ColdRoomResponse;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
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

    public ColdRoom fromCreateRequest(ColdRoomCreateRequest coldRoomCreateRequest){
        ColdRoom coldRoom = new ColdRoom();
        coldRoom.setName(coldRoomCreateRequest.getName());
        coldRoom.setLocation(coldRoomCreateRequest.getLocation());
        coldRoom.setEnabled(coldRoomCreateRequest.getEnabled());
        return coldRoom;
    }

    public List<ColdRoomResponse> toResponseList(List<ColdRoom> coldRooms) {
        return coldRooms.stream()
                .map(this::toResponse)
                .toList();
    }
}
