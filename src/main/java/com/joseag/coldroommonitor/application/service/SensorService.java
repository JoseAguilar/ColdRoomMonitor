package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.domain.model.SensorDevice;
import com.joseag.coldroommonitor.domain.repository.SensorDeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    private final SensorDeviceRepository repo;

    public SensorService(SensorDeviceRepository repo){
        this.repo = repo;
    }

    public List<SensorDevice> getAllActiveSensors(){
        return repo.findByEnabledTrue();
    }

    public SensorDevice create(SensorDevice sensor){
        return repo.save(sensor);
    }
}
