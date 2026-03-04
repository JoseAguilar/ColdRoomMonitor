package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.api.dto.response.ColdRoomResponse;
import com.joseag.coldroommonitor.api.dto.response.SensorDeviceResponse;
import com.joseag.coldroommonitor.api.mappers.SensorMapper;
import com.joseag.coldroommonitor.application.command.CreateSensorDeviceCommand;
import com.joseag.coldroommonitor.domain.exceptions.ColdRoomNotFoundException;
import com.joseag.coldroommonitor.domain.exceptions.SensorDeviceNotFoundException;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import com.joseag.coldroommonitor.domain.repository.ColdRoomRepository;
import com.joseag.coldroommonitor.domain.repository.SensorDeviceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    private final SensorDeviceRepository sensorDeviceRepository;
    private final ColdRoomRepository coldRoomRepository;
    private final SensorMapper sensorMapper;

    public SensorService(SensorDeviceRepository sensorDeviceRepository, ColdRoomRepository coldRoomRepository,
                         SensorMapper sensorMapper){
        this.sensorDeviceRepository = sensorDeviceRepository;
        this.coldRoomRepository = coldRoomRepository;
        this.sensorMapper = sensorMapper;
    }

    private SensorDevice getByIdOrThrow(Long id){
        return sensorDeviceRepository.findById(id)
                .orElseThrow(() -> new SensorDeviceNotFoundException(id));
    }

    public Page<SensorDeviceResponse> getAllActiveSensors(Pageable pageable){
        return sensorMapper.toResponsePage(sensorDeviceRepository.findByEnabledTrue(pageable));
    }

    public SensorDeviceResponse getById(Long id){
        return sensorMapper.toResponse(getByIdOrThrow(id));
    }

    public SensorDeviceResponse create(CreateSensorDeviceCommand command){

        ColdRoom coldRoom = coldRoomRepository.findById(command.coldRoomId()).orElseThrow(()-> new ColdRoomNotFoundException(command.coldRoomId()));
        SensorDevice sensorDevice = sensorMapper.fromCreateCommand(command, coldRoom);

        return sensorMapper.toResponse(sensorDeviceRepository.save(sensorDevice));
    }

    public List<SensorDeviceResponse> findByColdRoom(Long coldRoomId){
        if (!coldRoomRepository.existsById(coldRoomId)){
            throw new ColdRoomNotFoundException(coldRoomId);
        }
        return sensorMapper.toResponseList(sensorDeviceRepository.findByColdRoomId(coldRoomId));
    }
}
