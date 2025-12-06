package com.joseag.coldroommonitor.api.controller;

import com.joseag.coldroommonitor.application.dto.ColdRoomUpdateRequest;
import com.joseag.coldroommonitor.application.service.ColdRoomService;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("{id}")
    public ResponseEntity<ColdRoom> getFromId(@PathVariable Long id){
        Optional<ColdRoom> optionalColdRoom = service.findById(id);
        return optionalColdRoom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ColdRoom create(@RequestBody ColdRoom room){
        return service.create(room);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        boolean deleted = service.deleteById(id);
        return deleted?ResponseEntity.noContent().build():ResponseEntity.notFound().build();

    }

    @PutMapping("{id}")
    public ResponseEntity<ColdRoom> put(@PathVariable Long id, @RequestBody ColdRoomUpdateRequest room){
        Optional<ColdRoom> updated = service.updateColdRoom(id, room);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
