package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.mission.MissionInspectionRequest;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;

import java.util.List;
import java.util.UUID;

public interface MissionVehicleInspectionService {


    MissionVehicleInspectionDTO saveInspection(UUID missionId, MissionInspectionRequest request);

    MissionVehicleInspectionDTO getInspectionById(UUID id);

    List<MissionVehicleInspectionDTO> getAllInspections();

    MissionVehicleInspectionDTO getByMissionId(UUID missionId);

    void deleteInspection(UUID id);

}