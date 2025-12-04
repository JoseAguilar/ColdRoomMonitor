package com.joseag.coldroommonitor.api.controller;

import com.joseag.coldroommonitor.application.service.MeasurementService;
import com.joseag.coldroommonitor.domain.model.Measurement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/measurements")
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service){
        this.service = service;
    }

    @GetMapping
    public List<Measurement> getMeasurements(
            @RequestParam Long sensorId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to){ //2025-01-20T15:30:00


        return service.getMeasurements(sensorId, from, to);
    }
}
