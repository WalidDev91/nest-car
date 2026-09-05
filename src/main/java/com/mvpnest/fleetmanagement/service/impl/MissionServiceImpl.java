package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.mission.*;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.exception.AvailabilityConflictException;
import com.mvpnest.fleetmanagement.exception.ResourceNotFoundException;
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

        validateAvailability(request.getDriverId(), request.getVehicleId(), request.getStartDate(), request.getEndDate());

        Mission mission = Mission.builder().title(request.getTitle()).description(request.getDescription()).departureLocation(request.getDepartureLocation()).destinationLocation(request.getDestinationLocation()).startDate(request.getStartDate()).endDate(request.getEndDate()).status(request.getStatus() != null ? request.getStatus() : MissionStatus.PLANNED).driver(driver).vehicle(vehicle).build();

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

        validateAvailabilityForUpdate(request.getDriverId(), request.getVehicleId(), request.getStartDate(), request.getEndDate(), id);

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
        mission.setDepartureLocation(request.getDepartureLocation());
        mission.setDestinationLocation(request.getDestinationLocation());
        mission.setStartDate(request.getStartDate());
        mission.setEndDate(request.getEndDate());

        if (request.getStatus() != null) {
            mission.setStatus(request.getStatus());
        }

        return missionMapper.toDTO(missionRepository.save(mission));
    }

    @Override
    public List<MissionDTO> getMissionsByDepartureLocation(String departureLocation) {

        return missionRepository.findByDepartureLocation(departureLocation).stream().map(missionMapper::toDTO).toList();
    }

    @Override
    public List<MissionDTO> getMissionsByDestinationLocation(String destinationLocation) {

        return missionRepository.findByDestinationLocation(destinationLocation).stream().map(missionMapper::toDTO).toList();
    }

    @Override
    public void deleteMission(UUID id) {

        Mission mission = missionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mission not found"));

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

        validateAvailabilityForUpdate(request.getDriverId(), request.getVehicleId(), mission.getStartDate(), mission.getEndDate(), missionId);

        mission.setDriver(driver);
        mission.setVehicle(vehicle);

        return missionMapper.toDTO(missionRepository.save(mission));
    }

    @Override
    public MissionDTO updateDocumentsVerification(UUID missionId, Boolean verified) {

        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));

        mission.setDocumentsVerified(verified);

        mission.setDocumentsVerificationDate(verified == null ? null : LocalDateTime.now());

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

    // =====================================================
    // AVAILABILITY VALIDATION
    // =====================================================

    private void validateAvailability(UUID driverId, UUID vehicleId, LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate == null || endDate == null) {
            return;
        }

        boolean driverUnavailable = driverId != null && missionRepository.existsByDriverIdAndStartDateLessThanAndEndDateGreaterThan(driverId, endDate, startDate);

        boolean vehicleUnavailable = vehicleId != null && missionRepository.existsByVehicleIdAndStartDateLessThanAndEndDateGreaterThan(vehicleId, endDate, startDate);

        if (driverUnavailable && vehicleUnavailable) {
            throw new AvailabilityConflictException("Driver and vehicle are not available during this period");
        }

        if (driverUnavailable) {
            throw new AvailabilityConflictException("Driver is not available during this period");
        }

        if (vehicleUnavailable) {
            throw new AvailabilityConflictException("Vehicle is not available during this period");
        }
    }

    private void validateAvailabilityForUpdate(UUID driverId, UUID vehicleId, LocalDateTime startDate, LocalDateTime endDate, UUID missionId) {

        if (startDate == null || endDate == null) {
            return;
        }

        boolean driverUnavailable = driverId != null && missionRepository.existsByDriverIdAndStartDateLessThanAndEndDateGreaterThanAndIdNot(driverId, endDate, startDate, missionId);

        boolean vehicleUnavailable = vehicleId != null && missionRepository.existsByVehicleIdAndStartDateLessThanAndEndDateGreaterThanAndIdNot(vehicleId, endDate, startDate, missionId);

        if (driverUnavailable && vehicleUnavailable) {
            throw new AvailabilityConflictException("Driver and vehicle are not available during this period");
        }

        if (driverUnavailable) {
            throw new AvailabilityConflictException("Driver is not available during this period");
        }

        if (vehicleUnavailable) {
            throw new AvailabilityConflictException("Vehicle is not available during this period");
        }
    }
}