package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.api.dto.request.ColdRoomCreateRequest;
import com.joseag.coldroommonitor.api.dto.request.ColdRoomUpdateRequest;
import com.joseag.coldroommonitor.api.dto.response.ColdRoomResponse;
import com.joseag.coldroommonitor.api.mappers.ColdRoomMapper;
import com.joseag.coldroommonitor.domain.exceptions.ColdRoomNotFoundException;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import com.joseag.coldroommonitor.domain.repository.ColdRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ColdRoomApplicationService {
    private final ColdRoomRepository repo;
    private final ColdRoomMapper mapper;

    public ColdRoomApplicationService(ColdRoomRepository repo, ColdRoomMapper mapper){
        this.repo = repo;
        this.mapper = mapper;
    }

    private ColdRoom getByIdOrThrow(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new ColdRoomNotFoundException(id));
    }

    public List<ColdRoomResponse> findAll(){
        return mapper.toResponseList(repo.findAll());
    }

    public ColdRoomResponse getById(Long id){
        return mapper.toResponse(getByIdOrThrow(id));
    }

    /**
     * Elimina un cuarto frio por su id
     *
     * @param id identificador del cuarto frio
     * @throws ColdRoomNotFoundException si no existe un cuarto frio por ese id.
     */
    public void deleteById(Long id){
        ColdRoom coldRoom = getByIdOrThrow(id);
        repo.delete(coldRoom);
    }

    public ColdRoomResponse create(ColdRoomCreateRequest request){
        return mapper.toResponse(repo.save(mapper.fromCreateRequest(request)));
    }

    @Transactional
    public ColdRoomResponse updateColdRoom(Long id, ColdRoomUpdateRequest request){

        ColdRoom coldRoom = getByIdOrThrow(id);
        coldRoom.update(request);
        return mapper.toResponse(coldRoom);
    }

}
