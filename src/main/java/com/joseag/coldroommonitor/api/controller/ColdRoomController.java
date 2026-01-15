package com.joseag.coldroommonitor.api.controller;

import com.joseag.coldroommonitor.api.dto.request.ColdRoomCreateRequest;
import com.joseag.coldroommonitor.api.dto.request.ColdRoomUpdateRequest;
import com.joseag.coldroommonitor.api.dto.response.ColdRoomResponse;
import com.joseag.coldroommonitor.application.command.CreateColdRoomCommand;
import com.joseag.coldroommonitor.application.command.UpdateColdRoomCommand;
import com.joseag.coldroommonitor.application.service.ColdRoomApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/cold-rooms")
public class ColdRoomController {

    private final ColdRoomApplicationService service;

    public ColdRoomController(ColdRoomApplicationService service){
        this.service = service;
    }

    @GetMapping
    public Page<ColdRoomResponse> getAll(Pageable pageable){
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ColdRoomResponse getById(@PathVariable @Min(1) Long id){
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<ColdRoomResponse> create(@RequestBody @Valid ColdRoomCreateRequest request){

        var command = new CreateColdRoomCommand(
          request.getName(),
          request.getLocation(),
          request.getEnabled()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(command));
    }

    @PatchMapping("/{id}")
    public ColdRoomResponse partialUpdate(@PathVariable @Min(1) Long id, @Valid @RequestBody ColdRoomUpdateRequest request){

        var command = new UpdateColdRoomCommand(
          id,
          request.getName(),
          request.getLocation(),
          request.getEnabled()
        );

        return service.partialUpdate(command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(1) Long id){
        service.deleteById(id);
    }

}
