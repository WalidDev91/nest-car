package com.mvpnest.fleetmanagement.service.impl;


import com.mvpnest.fleetmanagement.dto.mission.MissionInspectionRequest;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import com.mvpnest.fleetmanagement.mapper.MissionVehicleInspectionMapper;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.MissionVehicleInspectionRepository;
import com.mvpnest.fleetmanagement.service.MissionVehicleInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MissionVehicleInspectionServiceImpl implements MissionVehicleInspectionService {


    private final MissionVehicleInspectionRepository inspectionRepository;

    private final MissionRepository missionRepository;

    private final MissionVehicleInspectionMapper mapper;


    @Override
    public MissionVehicleInspectionDTO saveInspection(UUID missionId, MissionInspectionRequest request) {

        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));

        MissionVehicleInspection inspection = inspectionRepository.findByMissionId(missionId).orElse(null);

        if (inspection == null) {

            inspection = MissionVehicleInspection.builder().mission(mission).inspectionDate(LocalDateTime.now()).build();
        }

        inspection.setMileage(request.getMileage());
        inspection.setFuelLevel(request.getFuelLevel());
        inspection.setNotes(request.getNotes());

        return mapper.toDTO(inspectionRepository.save(inspection));
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
    public MissionVehicleInspectionDTO getByMissionId(UUID missionId) {

        MissionVehicleInspection inspection = inspectionRepository.findByMissionId(missionId).orElseThrow(() -> new RuntimeException("No inspection found for mission"));

        return mapper.toDTO(inspection);
    }


    @Override
    public void deleteInspection(UUID id) {


        MissionVehicleInspection inspection = inspectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Inspection not found"));


        inspectionRepository.delete(inspection);

    }

}