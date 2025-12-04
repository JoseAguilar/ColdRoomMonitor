package com.joseag.coldroommonitor.api.controller;

import com.joseag.coldroommonitor.application.service.SensorService;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sensors")
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service){
        this.service = service;
    }

    @GetMapping
    public List<SensorDevice> getAllActive(){
        return service.getAllActiveSensors();
    }

    @PostMapping
    public SensorDevice create(@RequestBody SensorDevice sensor){
        return service.create(sensor);
    }
}
