package com.joseag.coldroommonitor.api.controller;

import com.joseag.coldroommonitor.api.dto.request.SensorDeviceCreateRequest;
import com.joseag.coldroommonitor.api.dto.request.SensorDeviceUpdateRequest;
import com.joseag.coldroommonitor.api.dto.response.SensorDeviceResponse;
import com.joseag.coldroommonitor.application.command.CreateSensorDeviceCommand;
import com.joseag.coldroommonitor.application.command.UpdateSensorDeviceCommand;
import com.joseag.coldroommonitor.application.service.SensorDeviceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1")
public class SensorDeviceController {

    private final SensorDeviceService service;

    public SensorDeviceController(SensorDeviceService service){
        this.service = service;
    }

    @GetMapping("/sensors")
    public Page<SensorDeviceResponse> getAll(Pageable pageable){
        return service.getAllSensors(pageable);
    }

    @GetMapping("/sensors/{id}")
    public SensorDeviceResponse getById(@PathVariable @Positive Long id){
        return service.getById(id);
    }

    @PostMapping("/sensors")
    public ResponseEntity<SensorDeviceResponse> create(@Valid @RequestBody SensorDeviceCreateRequest request){

        var command = new CreateSensorDeviceCommand(
          request.name(),
          request.coldRoomId(),
          request.enabled()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(command));
    }

    @PatchMapping("/sensors/{id}")
    public ResponseEntity<SensorDeviceResponse> partialUpdate(@PathVariable @Positive Long id, @Valid @RequestBody SensorDeviceUpdateRequest request){

        var command = new UpdateSensorDeviceCommand(
          id,
          request.name(),
          request.coldRoomId(),
          request.enabled()
        );

        return ResponseEntity.ok(service.partialUpdate(command));
    }

    @GetMapping("/cold-rooms/{coldRoomId}/sensors")
    public Page<SensorDeviceResponse> getAllSensors(@PathVariable @Positive Long coldRoomId, Pageable pageable){
        return service.findByColdRoom(coldRoomId, pageable);
    }

    @DeleteMapping("/sensors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(1) Long id){
        service.deleteById(id);
    }
}
