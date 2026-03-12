package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.api.dto.response.SensorDeviceResponse;
import com.joseag.coldroommonitor.api.mappers.SensorMapper;
import com.joseag.coldroommonitor.application.command.CreateSensorDeviceCommand;
import com.joseag.coldroommonitor.application.command.UpdateSensorDeviceCommand;
import com.joseag.coldroommonitor.domain.exceptions.ColdRoomNotFoundException;
import com.joseag.coldroommonitor.domain.exceptions.SensorDeviceNotFoundException;
import com.joseag.coldroommonitor.domain.model.ColdRoom;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import com.joseag.coldroommonitor.domain.repository.ColdRoomRepository;
import com.joseag.coldroommonitor.domain.repository.SensorDeviceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SensorDeviceService {

    private final SensorDeviceRepository sensorRepo;
    private final ColdRoomRepository coldRoomRepo;
    private final SensorMapper sensorMapper;

    public SensorDeviceService(SensorDeviceRepository sensorRepo, ColdRoomRepository coldRoomRepo,
                               SensorMapper sensorMapper){
        this.sensorRepo = sensorRepo;
        this.coldRoomRepo = coldRoomRepo;
        this.sensorMapper = sensorMapper;
    }

    private SensorDevice getByIdOrThrow(Long id){
        return sensorRepo.findById(id)
                .orElseThrow(() -> new SensorDeviceNotFoundException(id));
    }

    private ColdRoom getColdRoomByIdOrThrow(Long id){
        return coldRoomRepo.findById(id)
                .orElseThrow(() -> new ColdRoomNotFoundException(id));
    }

    /**
     *
     * Obtiene todos los sensores con paginacion.
     */
    @Transactional(readOnly = true)
    public Page<SensorDeviceResponse> getAllSensors(Pageable pageable){
        return sensorMapper.toResponsePage(sensorRepo.findAll(pageable));
    }

    /**
     *
     * Obtiene un sensor por su id.
     *
     * @throws SensorDeviceNotFoundException si el sensor no existe.
     */
    @Transactional(readOnly = true)
    public SensorDeviceResponse getById(Long id){
        return sensorMapper.toResponse(getByIdOrThrow(id));
    }

    /**
     * Crea un nuevo sensor asociado a un cuarto frio existente.
     *
     * @throws ColdRoomNotFoundException si el cuarto frio no existe.
     * */
    @Transactional(readOnly = true)
    public SensorDeviceResponse create(CreateSensorDeviceCommand command){

        ColdRoom coldRoom = getColdRoomByIdOrThrow(command.coldRoomId());
        SensorDevice sensorDevice = sensorMapper.fromCreateCommand(command, coldRoom);

        return sensorMapper.toResponse(sensorRepo.save(sensorDevice));
    }

    @Transactional(readOnly = true)
    public Page<SensorDeviceResponse> findByColdRoom(Long coldRoomId, Pageable pageable){
        getColdRoomByIdOrThrow(coldRoomId);
        return sensorMapper.toResponsePage(sensorRepo.findByColdRoomId(coldRoomId, pageable));
    }

    /**
     *
     * Actualiza parcialmente un sensor.
     *
     * Solo los campos no nulos del comando seran aplicados.
     * Si se proporciona coldRoomId, el sensor sera reasignado al nuevo cuarto frio.
     *
     * @throws SensorDeviceNotFoundException si el sensor no existe.
     * @throws ColdRoomNotFoundException si el cuarto frio no existe.
     */
    @Transactional
    public SensorDeviceResponse partialUpdate(UpdateSensorDeviceCommand command){

        SensorDevice sensorDevice = getByIdOrThrow(command.id());

        sensorDevice.update(command);

        if (command.coldRoomId() != null){
            ColdRoom newColdRoom = getColdRoomByIdOrThrow(command.coldRoomId());
            sensorDevice.setColdRoom(newColdRoom);
        }

        return sensorMapper.toResponse(sensorDevice);
    }
}
