package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.CreateMissionVehicleInspectionRequest;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.UpdateMissionVehicleInspectionRequest;

import java.util.List;
import java.util.UUID;

public interface MissionVehicleInspectionService {


    MissionVehicleInspectionDTO createInspection(CreateMissionVehicleInspectionRequest request);

    MissionVehicleInspectionDTO getInspectionById(UUID id);

    List<MissionVehicleInspectionDTO> getAllInspections();

    MissionVehicleInspectionDTO getByMissionId(UUID missionId);

    MissionVehicleInspectionDTO updateInspection(UUID id, UpdateMissionVehicleInspectionRequest request);

    void deleteInspection(UUID id);

}