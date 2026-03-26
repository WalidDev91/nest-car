package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.Mission;

import java.util.List;
import java.util.UUID;

public interface MissionService {

    Mission createMission(Mission mission);

    Mission getMissionById(UUID id);

    List<Mission> getAllMissions();

    Mission updateMission(UUID id, Mission mission);

    void deleteMission(UUID id);
}
