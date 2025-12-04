package com.joseag.coldroommonitor.api.controller;

import com.joseag.coldroommonitor.application.service.ColdRoomService;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cold-rooms")
public class ColdRoomController {

    private final ColdRoomService service;

    public ColdRoomController(ColdRoomService service){
        this.service = service;
    }

    @GetMapping
    public List<ColdRoom> getAll(){
        return service.findAll();
    }

    @PostMapping
    public ColdRoom create(@RequestBody ColdRoom room){
        return service.create(room);
    }
}
