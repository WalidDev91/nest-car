package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MissionRepository extends JpaRepository<Mission, UUID> {

    List<Mission> findByDriverId(UUID driverId);

    List<Mission> findByStatus(MissionStatus status);

}