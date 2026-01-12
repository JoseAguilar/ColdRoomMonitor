package com.joseag.coldroommonitor.domain.repository;

import com.joseag.coldroommonitor.domain.model.SensorDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorDeviceRepository extends JpaRepository<SensorDevice, Long> {
    List<SensorDevice> findByEnabledTrue();
    List<SensorDevice> findByColdRoomId(Long coldRoomId);
}
