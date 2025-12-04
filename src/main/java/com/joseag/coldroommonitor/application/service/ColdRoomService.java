package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.domain.model.ColdRoom;
import com.joseag.coldroommonitor.domain.repository.ColdRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColdRoomService {
    private final ColdRoomRepository repo;

    public ColdRoomService(ColdRoomRepository repo){
        this.repo = repo;
    }

    public List<ColdRoom> findAll(){
        return repo.findAll();
    }

    public ColdRoom create(ColdRoom room){
        return repo.save(room);
    }
}
