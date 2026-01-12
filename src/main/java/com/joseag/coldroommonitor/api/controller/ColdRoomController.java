package com.joseag.coldroommonitor.api.controller;

import com.joseag.coldroommonitor.api.dto.request.ColdRoomCreateRequest;
import com.joseag.coldroommonitor.api.dto.request.ColdRoomUpdateRequest;
import com.joseag.coldroommonitor.api.dto.response.ColdRoomResponse;
import com.joseag.coldroommonitor.application.service.ColdRoomApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/cold-rooms")
public class ColdRoomController {

    private final ColdRoomApplicationService service;

    public ColdRoomController(ColdRoomApplicationService service){
        this.service = service;
    }

    @GetMapping
    public List<ColdRoomResponse> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ColdRoomResponse getById(@PathVariable @Min(1) Long id){
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<ColdRoomResponse> create(@RequestBody @Valid ColdRoomCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PatchMapping("/{id}")
    public ColdRoomResponse partialUpdate(@PathVariable @Min(1) Long id, @Valid @RequestBody ColdRoomUpdateRequest request){
        return service.updateColdRoom(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(1) Long id){
        service.deleteById(id);
    }

}
