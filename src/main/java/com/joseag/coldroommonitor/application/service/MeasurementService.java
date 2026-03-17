package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.api.dto.response.MeasurementItemResponse;
import com.joseag.coldroommonitor.api.dto.response.SensorMeasurementResponse;
import com.joseag.coldroommonitor.api.mappers.MeasurementMapper;
import com.joseag.coldroommonitor.application.command.CreateMeasurementCommand;
import com.joseag.coldroommonitor.application.command.SearchMeasurementCommand;
import com.joseag.coldroommonitor.domain.exceptions.SensorDeviceNotFoundException;
import com.joseag.coldroommonitor.domain.model.Measurement;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import com.joseag.coldroommonitor.domain.repository.MeasurementRepository;
import com.joseag.coldroommonitor.domain.repository.SensorDeviceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepo;
    private final SensorDeviceRepository sensorRepo;
    private final MeasurementMapper mapper;

    public MeasurementService(MeasurementRepository measurementRepo, SensorDeviceRepository sensorRepo,
                              MeasurementMapper mapper){
        this.measurementRepo = measurementRepo;
        this.sensorRepo = sensorRepo;
        this.mapper = mapper;
    }

    public SensorMeasurementResponse createMeasurement(CreateMeasurementCommand command){
        SensorDevice sensorDevice = this.getSensorByIdOrThrow(command.id());

        List<Measurement> savedMeasurements = command.measurements().stream().map(item ->
                saveMeasurement(sensorDevice, item.currentValue(), item.measuredAt())
        ).toList();

        return mapper.toSensorResponse(command.id(), savedMeasurements);
    }

    private SensorDevice getSensorByIdOrThrow(Long id){
        return this.sensorRepo.findByIdAndEnabledTrue(id).orElseThrow(()-> new SensorDeviceNotFoundException(id));
    }

    private Measurement saveMeasurement(SensorDevice sensor, BigDecimal value, LocalDateTime measuredAt){
        Measurement m = new Measurement();
        m.setSensor(sensor);
        m.setMeasuredAt(measuredAt);
        m.setValue(value);
        return measurementRepo.save(m);
    }

    public Page<MeasurementItemResponse> getSensorMeasurements(SearchMeasurementCommand command, Long sensorId, Pageable pageable){
        this.getSensorByIdOrThrow(sensorId);
        return getMeasurementsBySensor(command, sensorId, pageable);
    }

    private Page<MeasurementItemResponse> getMeasurementsBySensor(SearchMeasurementCommand command, Long sensorId, Pageable pageable){
        if (command.from() != null && command.to() != null){
            if (command.from().isAfter(command.to())){
                throw new IllegalArgumentException("'from' must be before or equal 'to'");
            }
            Page<Measurement> measurementList = measurementRepo.findBySensorIdAndMeasuredAtBetween(sensorId, command.from(), command.to(), pageable);
            return mapper.toSensorResponse(measurementList);
        } else if (command.to() != null){
            Page<Measurement> measurementList = measurementRepo.findBySensorIdAndMeasuredAtLessThanEqual(sensorId, command.to(), pageable);
            return mapper.toSensorResponse(measurementList);
        } else if (command.from() != null){
            Page<Measurement> measurementList = measurementRepo.findBySensorIdAndMeasuredAtGreaterThanEqual(sensorId, command.from(), pageable);
            return mapper.toSensorResponse(measurementList);
        } else {
            Page<Measurement> measurementList = measurementRepo.findBySensorId(sensorId, pageable);
            return mapper.toSensorResponse(measurementList);
        }
    }

}
