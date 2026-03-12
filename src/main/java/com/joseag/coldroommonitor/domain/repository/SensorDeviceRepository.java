package com.joseag.coldroommonitor.domain.repository;

import com.joseag.coldroommonitor.domain.model.SensorDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SensorDeviceRepository extends JpaRepository<SensorDevice, Long> {
    Page<SensorDevice> findByEnabledTrue(Pageable pageable);
    Page<SensorDevice> findByColdRoomId(Long coldRoomId, Pageable pageable);
}
