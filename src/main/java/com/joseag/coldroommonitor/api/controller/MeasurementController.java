package com.joseag.coldroommonitor.api.controller;

import com.joseag.coldroommonitor.api.dto.request.MeasurementCreateRequest;
import com.joseag.coldroommonitor.api.dto.request.MeasurementSearchRequest;
import com.joseag.coldroommonitor.api.dto.response.ColdRoomMeasurementResponse;
import com.joseag.coldroommonitor.api.dto.response.MeasurementItemResponse;
import com.joseag.coldroommonitor.api.dto.response.SensorMeasurementResponse;
import com.joseag.coldroommonitor.api.mappers.MeasurementMapper;
import com.joseag.coldroommonitor.application.service.MeasurementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/")
public class MeasurementController {

    private final MeasurementService service;
    private final MeasurementMapper mapper;

    public MeasurementController(MeasurementService service, MeasurementMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/sensors/{sensorId}/measurements")
    public ResponseEntity<Page<MeasurementItemResponse>> getSensorMeasurements(
            @Valid @ModelAttribute MeasurementSearchRequest request,
            @PathVariable @Positive Long sensorId,
            Pageable pageable){
        var command = this.mapper.toCommand(request);
        return ResponseEntity.status(HttpStatus.OK).body(service.getSensorMeasurements(command, sensorId, pageable));
    }

    @PostMapping("/measurements")
    public ResponseEntity<SensorMeasurementResponse> create(@Valid @RequestBody MeasurementCreateRequest request){
        var command = this.mapper.toCommand(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createMeasurement(command));
    }



}
