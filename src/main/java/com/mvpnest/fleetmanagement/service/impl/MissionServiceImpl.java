package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.mission.*;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.mapper.MissionMapper;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.MissionVehicleInspectionRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.MissionService;
import com.mvpnest.fleetmanagement.service.MissionVehicleInspectionService;
import com.mvpnest.fleetmanagement.service.MissionVehiclePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final MissionVehicleInspectionRepository inspectionRepository;
    private final MissionVehicleInspectionService inspectionService;
    private final MissionVehiclePhotoService photoService;
    private final MissionMapper missionMapper;

    @Override
    public MissionDTO createMission(CreateMissionRequest request) {

        User driver = null;

        if (request.getStartDate() != null && request.getEndDate() != null && request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        if (request.getDriverId() != null) {

            driver = userRepository.findById(request.getDriverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

            if (driver.getRole() != RoleType.DRIVER) {
                throw new RuntimeException("Selected user is not a driver");
            }
        }

        Vehicle vehicle = null;

        if (request.getVehicleId() != null) {

            vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(() -> new RuntimeException("Vehicle not found"));
        }


        Mission mission = Mission.builder().title(request.getTitle()).description(request.getDescription()).startDate(request.getStartDate()).endDate(request.getEndDate()).status(request.getStatus() != null ? request.getStatus() : MissionStatus.PLANNED).driver(driver).vehicle(vehicle).build();


        return missionMapper.toDTO(missionRepository.save(mission));
    }

    @Override
    public MissionDTO getMissionById(UUID id) {

        Mission mission = missionRepository.findById(id).orElseThrow(() -> new RuntimeException("Mission not found"));

        return missionMapper.toDTO(mission);
    }

    @Override
    public List<MissionDTO> getAllMissions() {

        return missionRepository.findAll().stream().map(missionMapper::toDTO).toList();
    }

    @Override
    public MissionDTO updateMission(UUID id, UpdateMissionRequest request) {

        if (request.getStartDate() != null && request.getEndDate() != null && request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        Mission mission = missionRepository.findById(id).orElseThrow(() -> new RuntimeException("Mission not found"));


        if (request.getDriverId() != null) {

            User driver = userRepository.findById(request.getDriverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

            if (driver.getRole() != RoleType.DRIVER) {
                throw new RuntimeException("Selected user is not a driver");
            }

            mission.setDriver(driver);

        } else {
            mission.setDriver(null);
        }


        if (request.getVehicleId() != null) {

            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(() -> new RuntimeException("Vehicle not found"));

            mission.setVehicle(vehicle);

        } else {
            mission.setVehicle(null);
        }


        mission.setTitle(request.getTitle());
        mission.setDescription(request.getDescription());
        mission.setStartDate(request.getStartDate());
        mission.setEndDate(request.getEndDate());
        if (request.getStatus() != null) {
            mission.setStatus(request.getStatus());
        }


        return missionMapper.toDTO(missionRepository.save(mission));
    }

    @Override
    public void deleteMission(UUID id) {

        Mission mission = missionRepository.findById(id).orElseThrow(() -> new RuntimeException("Mission not found"));

        missionRepository.delete(mission);
    }

    @Override
    public List<MissionDTO> getMissionsByVehicleId(UUID vehicleId) {

        return missionRepository.findByVehicleId(vehicleId).stream().map(missionMapper::toDTO).toList();

    }

    @Override
    public List<MissionDTO> getMissionsByStatus(MissionStatus status) {

        return missionRepository.findByStatus(status).stream().map(missionMapper::toDTO).toList();
    }

    @Override
    public MissionDTO assignMission(UUID missionId, MissionAssignmentRequest request) {

        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));

        User driver = null;

        if (request.getDriverId() != null) {

            driver = userRepository.findById(request.getDriverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

            if (driver.getRole() != RoleType.DRIVER) {
                throw new RuntimeException("Selected user is not a driver");
            }
        }

        Vehicle vehicle = null;

        if (request.getVehicleId() != null) {

            vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(() -> new RuntimeException("Vehicle not found"));
        }

        mission.setDriver(driver);
        mission.setVehicle(vehicle);

        return missionMapper.toDTO(missionRepository.save(mission));
    }

    @Override
    public MissionDTO verifyDocuments(UUID missionId) {

        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));

        mission.setDocumentsVerified(true);
        mission.setDocumentsVerificationDate(LocalDateTime.now());

        return missionMapper.toDTO(missionRepository.save(mission));
    }

    @Override
    public MissionDTO saveInspection(UUID missionId, MissionInspectionRequest request) {

        inspectionService.saveInspection(missionId, request);

        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));

        return missionMapper.toDTO(mission);
    }

    @Override
    public MissionDTO uploadInspectionPhoto(UUID missionId, MultipartFile file, String description) {

        MissionVehicleInspection inspection = inspectionRepository.findByMissionId(missionId).orElseThrow(() -> new RuntimeException("Inspection not found"));

        photoService.uploadPhoto(file, inspection.getId(), description);

        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));

        return missionMapper.toDTO(mission);
    }

    @Override
    public MissionDTO deleteInspection(UUID missionId) {

        MissionVehicleInspection inspection = inspectionRepository.findByMissionId(missionId).orElseThrow(() -> new RuntimeException("Inspection not found"));

        Mission mission = inspection.getMission();

        mission.setVehicleInspection(null);
        inspection.setMission(null);

        missionRepository.save(mission);

        return missionMapper.toDTO(mission);
    }

    @Override
    public MissionDTO deleteInspectionPhoto(UUID missionId, UUID photoId) {

        photoService.deletePhoto(photoId);

        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));

        return missionMapper.toDTO(mission);
    }

}