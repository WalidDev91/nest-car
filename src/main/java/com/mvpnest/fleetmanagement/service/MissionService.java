package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.mission.CreateMissionRequest;
import com.mvpnest.fleetmanagement.dto.mission.MissionDTO;
import com.mvpnest.fleetmanagement.dto.mission.UpdateMissionRequest;

import java.util.List;
import java.util.UUID;

public interface MissionService {

    MissionDTO createMission(CreateMissionRequest request);

    MissionDTO getMissionById(UUID id);

    List<MissionDTO> getAllMissions();

    MissionDTO updateMission(UUID id, UpdateMissionRequest request);

    void deleteMission(UUID id);
}