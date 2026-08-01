package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.mission.*;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MissionService {

    MissionDTO createMission(CreateMissionRequest request);

    MissionDTO getMissionById(UUID id);

    List<MissionDTO> getAllMissions();

    MissionDTO updateMission(UUID id, UpdateMissionRequest request);

    void deleteMission(UUID id);

    List<MissionDTO> getMissionsByVehicleId(UUID vehicleId);

    List<MissionDTO> getMissionsByStatus(MissionStatus status);

    MissionDTO assignMission(UUID missionId, MissionAssignmentRequest request);

    MissionDTO verifyDocuments(UUID missionId);

    MissionDTO saveInspection(UUID missionId, MissionInspectionRequest request);

    MissionDTO uploadInspectionPhoto(UUID missionId, MultipartFile file, String description);

    MissionDTO deleteInspectionPhoto(UUID missionId, UUID photoId);
}