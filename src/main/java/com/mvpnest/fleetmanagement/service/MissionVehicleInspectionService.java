package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;

import java.util.List;
import java.util.UUID;

public interface MissionVehicleInspectionService {

    MissionVehicleInspection createInspection(MissionVehicleInspection inspection);

    MissionVehicleInspection getInspectionById(UUID id);

    List<MissionVehicleInspection> getAllInspections();

    MissionVehicleInspection getByMissionId(UUID missionId);

    MissionVehicleInspection updateInspection(UUID id, MissionVehicleInspection inspection);

    void deleteInspection(UUID id);

}
