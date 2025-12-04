package com.joseag.coldroommonitor.domain.repository;

import com.joseag.coldroommonitor.domain.model.ColdRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColdRoomRepository extends JpaRepository<ColdRoom, Long>{}
