package com.joseag.coldroommonitor.domain.repository;

import com.joseag.coldroommonitor.domain.model.ColdRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ColdRoomRepository extends JpaRepository<ColdRoom, Long>{
    Optional<ColdRoom> findByIdAndEnabledTrue(Long id);
    Page<ColdRoom> findAllByEnabledTrue(Pageable pageable);
}
