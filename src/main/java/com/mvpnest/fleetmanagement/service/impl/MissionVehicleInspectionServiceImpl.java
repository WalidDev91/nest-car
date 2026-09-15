package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionInspectionRequest;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import com.mvpnest.fleetmanagement.mapper.MissionVehicleInspectionMapper;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.MissionVehicleInspectionRepository;
import com.mvpnest.fleetmanagement.service.MissionVehicleInspectionService;
import com.mvpnest.fleetmanagement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionVehicleInspectionServiceImpl implements MissionVehicleInspectionService {

    private final MissionVehicleInspectionRepository inspectionRepository;
    private final MissionRepository missionRepository;
    private final MissionVehicleInspectionMapper mapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public MissionVehicleInspectionDTO saveInspection(UUID missionId, MissionInspectionRequest request) {

        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));

        MissionVehicleInspection inspection = inspectionRepository.findByMissionIdAndInspectionType(missionId, request.getInspectionType()).orElse(null);

        boolean isNewInspection = inspection == null;

        if (isNewInspection) {
            inspection = MissionVehicleInspection.builder().mission(mission).inspectionType(request.getInspectionType()).inspectionDate(LocalDateTime.now()).build();
        }

        inspection.setMileage(request.getMileage());
        inspection.setFuelLevel(request.getFuelLevel());
        inspection.setTirePressure(request.getTirePressure());
        inspection.setOilChange(request.getOilChange());
        inspection.setWaterCheck(request.getWaterCheck());
        inspection.setPartsCondition(request.getPartsCondition());
        inspection.setRepairStatus(request.getRepairStatus());
        inspection.setAccidentOccurred(request.getAccidentOccurred() != null ? request.getAccidentOccurred() : false);
        inspection.setNotes(request.getNotes());

        MissionVehicleInspection saved = inspectionRepository.save(inspection);

        if (isNewInspection) {
            notificationService.notifyInspectionCompleted(saved);
        }

        return mapper.toDTO(saved);
    }

    @Override
    public MissionVehicleInspectionDTO getInspectionById(UUID id) {

        MissionVehicleInspection inspection = inspectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Inspection not found"));

        return mapper.toDTO(inspection);
    }

    @Override
    public List<MissionVehicleInspectionDTO> getAllInspections() {

        return inspectionRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<MissionVehicleInspectionDTO> getByMissionId(UUID missionId) {

        if (!missionRepository.existsById(missionId)) {
            throw new RuntimeException("Mission not found");
        }

        return inspectionRepository.findByMissionId(missionId).stream().map(mapper::toDTO).toList();
    }

    @Override
    public void deleteInspection(UUID id) {

        MissionVehicleInspection inspection = inspectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Inspection not found"));

        inspectionRepository.delete(inspection);
    }
}