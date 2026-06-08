package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.mission.CreateMissionRequest;
import com.mvpnest.fleetmanagement.dto.mission.MissionDTO;
import com.mvpnest.fleetmanagement.dto.mission.UpdateMissionRequest;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import com.mvpnest.fleetmanagement.mapper.MissionMapper;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final MissionMapper missionMapper;

    @Override
    public MissionDTO createMission(CreateMissionRequest request) {

        User driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Mission mission = Mission.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(MissionStatus.PLANNED)
                .driver(driver)
                .vehicle(vehicle)
                .build();

        return missionMapper.toDTO(
                missionRepository.save(mission)
        );
    }

    @Override
    public MissionDTO getMissionById(UUID id) {

        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        return missionMapper.toDTO(mission);
    }

    @Override
    public List<MissionDTO> getAllMissions() {

        return missionRepository.findAll()
                .stream()
                .map(missionMapper::toDTO)
                .toList();
    }

    @Override
    public MissionDTO updateMission(UUID id, UpdateMissionRequest request) {

        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        User driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        mission.setTitle(request.getTitle());
        mission.setDescription(request.getDescription());
        mission.setStartDate(request.getStartDate());
        mission.setEndDate(request.getEndDate());
        mission.setStatus(request.getStatus());
        mission.setDriver(driver);
        mission.setVehicle(vehicle);

        return missionMapper.toDTO(
                missionRepository.save(mission)
        );
    }

    @Override
    public void deleteMission(UUID id) {

        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        missionRepository.delete(mission);
    }
}