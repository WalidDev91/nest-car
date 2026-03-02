package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MissionVehicleInspectionRepository extends JpaRepository<MissionVehicleInspection, UUID> {
}
