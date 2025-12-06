package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.application.dto.ColdRoomUpdateRequest;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import com.joseag.coldroommonitor.domain.repository.ColdRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColdRoomService {
    private final ColdRoomRepository repo;

    public ColdRoomService(ColdRoomRepository repo){
        this.repo = repo;
    }

    public List<ColdRoom> findAll(){
        return repo.findAll();
    }

    public Optional<ColdRoom> findById(Long id){
        return repo.findById(id);
    }

    public boolean deleteById(Long id){
        return repo.findById(id).map(coldRoom -> {
            repo.delete(coldRoom);
            return true;
        }).orElse(false);
    }

    public ColdRoom create(ColdRoom room){
        return repo.save(room);
    }

    public Optional<ColdRoom> updateColdRoom(Long id, ColdRoomUpdateRequest request){
        return repo.findById(id).map(existing -> {
            if (request.getName() != null){
                existing.setName(request.getName());
            }

            if (request.getLocation() != null){
                existing.setLocation(request.getLocation());
            }

            if (request.getEnabled() != null){
                existing.setEnabled(request.getEnabled());
            }
            return repo.save(existing);
        });
    }

}
