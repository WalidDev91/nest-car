package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;

    @Override
    public Mission createMission(Mission mission) {
        return missionRepository.save(mission);
    }

    @Override
    public Mission getMissionById(UUID id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + id));
    }

    @Override
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    @Override
    public Mission updateMission(UUID id, Mission mission) {
        Mission existing = getMissionById(id);

        existing.setTitle(mission.getTitle());
        existing.setDescription(mission.getDescription());
        existing.setStartDate(mission.getStartDate());
        existing.setEndDate(mission.getEndDate());
        existing.setStatus(mission.getStatus());
        existing.setDriver(mission.getDriver());
        existing.setVehicle(mission.getVehicle());

        return missionRepository.save(existing);
    }

    @Override
    public void deleteMission(UUID id) {
        Mission mission = getMissionById(id);
        missionRepository.delete(mission);
    }
}
