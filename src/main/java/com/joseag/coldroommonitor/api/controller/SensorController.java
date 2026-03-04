package com.joseag.coldroommonitor.api.controller;

import com.joseag.coldroommonitor.api.dto.request.SensorDeviceCreateRequest;
import com.joseag.coldroommonitor.api.dto.response.SensorDeviceResponse;
import com.joseag.coldroommonitor.application.command.CreateSensorDeviceCommand;
import com.joseag.coldroommonitor.application.service.SensorService;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service){
        this.service = service;
    }

    @GetMapping("sensors")
    public Page<SensorDeviceResponse> getAllActive(Pageable pageable){
        return service.getAllActiveSensors(pageable);
    }

    @GetMapping("sensors/{id}")
    public SensorDeviceResponse getById(@PathVariable @Min(1) Long id){
        return service.getById(id);
    }

    @PostMapping("sensors")
    public ResponseEntity<SensorDeviceResponse> create(@RequestBody @Valid SensorDeviceCreateRequest request){

        var command = new CreateSensorDeviceCommand(
          request.getName(),
          request.getColdRoomId(),
          request.getEnabled()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(command));
    }

    @GetMapping("cold-rooms/{id}/sensors")
    public List<SensorDeviceResponse> getAllSensors(@PathVariable Long id){
        return service.findByColdRoom(id);
    }
}
