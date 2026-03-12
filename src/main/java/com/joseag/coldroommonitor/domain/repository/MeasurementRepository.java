package com.joseag.coldroommonitor.domain.repository;

import com.joseag.coldroommonitor.domain.model.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    Page<Measurement> findBySensorIdAndMeasuredAtBetween(Long sensorId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    Page<Measurement> findBySensorIdAndMeasuredAtGreaterThanEqual(Long sensorId, LocalDateTime from, Pageable pageable);
    Page<Measurement> findBySensorIdAndMeasuredAtLessThanEqual(Long sensorId, LocalDateTime to, Pageable pageable);
    Page<Measurement> findBySensorId(Long sensorId, Pageable pageable);
}
