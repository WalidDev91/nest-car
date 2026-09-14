package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MissionVehicleInspectionRepository extends JpaRepository<MissionVehicleInspection, UUID> {

    List<MissionVehicleInspection> findByMissionId(UUID missionId);

    Optional<MissionVehicleInspection> findByMissionIdAndInspectionType(UUID missionId, com.mvpnest.fleetmanagement.enums.InspectionType inspectionType);
}