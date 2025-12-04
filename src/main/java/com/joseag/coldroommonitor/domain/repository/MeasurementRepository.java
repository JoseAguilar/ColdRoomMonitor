package com.joseag.coldroommonitor.domain.repository;

import com.joseag.coldroommonitor.domain.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findBySensorIdAndTimestampBetween(Long sensorId, LocalDateTime from, LocalDateTime to);
}
