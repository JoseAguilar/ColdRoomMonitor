package com.joseag.coldroommonitor.api.mappers;

import com.joseag.coldroommonitor.api.dto.response.ColdRoomResponse;
import com.joseag.coldroommonitor.api.dto.response.SensorDeviceResponse;
import com.joseag.coldroommonitor.application.command.CreateColdRoomCommand;
import com.joseag.coldroommonitor.application.command.CreateSensorDeviceCommand;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorMapper {

    public SensorDeviceResponse toResponse(SensorDevice sensorDevice){
        return new SensorDeviceResponse(
          sensorDevice.getId(),
          sensorDevice.getIdOfColdRoom(),
          sensorDevice.getName(),
          sensorDevice.isEnabled()
        );
    }

    public List<SensorDeviceResponse> toResponseList(List<SensorDevice> coldRooms) {
        return coldRooms.stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<SensorDeviceResponse> toResponsePage(Page<SensorDevice> sensorDevices) {
        return sensorDevices.map(this::toResponse);
    }

    public SensorDevice fromCreateCommand(CreateSensorDeviceCommand command, ColdRoom coldRoom){
        return new SensorDevice(command.name(), coldRoom, command.enabled());
    }
}
