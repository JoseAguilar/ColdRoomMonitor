package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.api.dto.response.SensorDeviceResponse;
import com.joseag.coldroommonitor.api.mappers.SensorMapper;
import com.joseag.coldroommonitor.domain.exceptions.ColdRoomNotFoundException;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import com.joseag.coldroommonitor.domain.repository.ColdRoomRepository;
import com.joseag.coldroommonitor.domain.repository.SensorDeviceRepository;
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

    public List<SensorDevice> getAllActiveSensors(){
        return sensorDeviceRepository.findByEnabledTrue();
    }

    public SensorDevice create(SensorDevice sensor){
        return sensorDeviceRepository.save(sensor);
    }

    public List<SensorDeviceResponse> findByColdRoom(Long coldRoomId){
        if (!coldRoomRepository.existsById(coldRoomId)){
            throw new ColdRoomNotFoundException(coldRoomId);
        }
        return sensorMapper.toResponseList(sensorDeviceRepository.findByColdRoomId(coldRoomId));
    }
}
