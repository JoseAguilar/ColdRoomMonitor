package com.joseag.coldroommonitor.application.service;

import com.joseag.coldroommonitor.domain.model.Measurement;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import com.joseag.coldroommonitor.domain.repository.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementService {
    private final MeasurementRepository repo;

    public MeasurementService(MeasurementRepository repo){
        this.repo = repo;
    }

    public Measurement saveMeasurement(SensorDevice sensor, Double value){
        Measurement m = new Measurement();
        m.setSensor(sensor);
        m.setTimestamp(LocalDateTime.now());
        m.setValue(value);
        return repo.save(m);
    }

    public List<Measurement> getMeasurements(Long sensorId, LocalDateTime from, LocalDateTime to){
        return repo.findBySensorIdAndTimestampBetween(sensorId, from, to);
    }
}
