package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.api.dto.response.ColdRoomResponse;
import com.joseag.coldroommonitor.api.mappers.ColdRoomMapper;
import com.joseag.coldroommonitor.application.command.CreateColdRoomCommand;
import com.joseag.coldroommonitor.application.command.UpdateColdRoomCommand;
import com.joseag.coldroommonitor.domain.exceptions.ColdRoomNotFoundException;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import com.joseag.coldroommonitor.domain.repository.ColdRoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Page<ColdRoomResponse> findAll(Pageable pageable){
        return mapper.toResponsePage(repo.findAll(pageable));
    }


    /**
     * Obtiene un cuarto frio por medio de su respectivo id
     *
     * @param id identificador del cuarto frio
     * @return respuesta del cuarto frio
     * @throws ColdRoomNotFoundException si no existe un cuarto frio por ese id.
     */
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

    /**
     * Crea un cuarto frio
     *
     * @param command datos necesarios para crear el cuarto frio
     * @return respuesta del cuarto frio creado.
     */
    public ColdRoomResponse create(CreateColdRoomCommand command){
        ColdRoom coldRoom = mapper.fromCreateCommand(command);
        return mapper.toResponse(repo.save(coldRoom));
    }

    /**
     * Actualiza parcial o totalmente un cuarto frio dependiendo la solicitud
     *
     * @param command datos para actualizar el cuarto frio
     * @return respuesta del cuarto frio
     * Applies a partial update: only non-null fields from the command are applied.
     * @throws ColdRoomNotFoundException si no existe un cuarto frio por ese id
     */
    @Transactional
    public ColdRoomResponse partialUpdate(UpdateColdRoomCommand command){

        ColdRoom coldRoom = getByIdOrThrow(command.id());
        coldRoom.update(command);
        return mapper.toResponse(coldRoom);
    }

}
