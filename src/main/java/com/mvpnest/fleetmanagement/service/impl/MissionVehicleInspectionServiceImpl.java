package com.mvpnest.fleetmanagement.service.impl;


import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.CreateMissionVehicleInspectionRequest;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.UpdateMissionVehicleInspectionRequest;
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
public class MissionVehicleInspectionServiceImpl
        implements MissionVehicleInspectionService {


    private final MissionVehicleInspectionRepository inspectionRepository;

    private final MissionRepository missionRepository;

    private final MissionVehicleInspectionMapper mapper;



    @Override
    public MissionVehicleInspectionDTO createInspection(
            CreateMissionVehicleInspectionRequest request
    ) {


        Mission mission = missionRepository.findById(request.getMissionId())
                .orElseThrow(() ->
                        new RuntimeException("Mission not found")
                );


        MissionVehicleInspection inspection =
                MissionVehicleInspection.builder()
                        .inspectionDate(LocalDateTime.now())
                        .notes(request.getNotes())
                        .mileage(request.getMileage())
                        .fuelLevel(request.getFuelLevel())
                        .mission(mission)
                        .build();


        return mapper.toDTO(
                inspectionRepository.save(inspection)
        );
    }



    @Override
    public MissionVehicleInspectionDTO getInspectionById(UUID id) {


        MissionVehicleInspection inspection =
                inspectionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Inspection not found")
                        );


        return mapper.toDTO(inspection);

    }



    @Override
    public List<MissionVehicleInspectionDTO> getAllInspections() {


        return inspectionRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();

    }



    @Override
    public MissionVehicleInspectionDTO getByMissionId(UUID missionId) {


        MissionVehicleInspection inspection =
                inspectionRepository.findByMissionId(missionId);


        if(inspection == null){
            throw new RuntimeException(
                    "No inspection found for mission"
            );
        }


        return mapper.toDTO(inspection);

    }



    @Override
    public MissionVehicleInspectionDTO updateInspection(
            UUID id,
            UpdateMissionVehicleInspectionRequest request
    ) {


        MissionVehicleInspection existing =
                inspectionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Inspection not found")
                        );


        existing.setNotes(request.getNotes());

        existing.setMileage(request.getMileage());

        existing.setFuelLevel(request.getFuelLevel());


        return mapper.toDTO(
                inspectionRepository.save(existing)
        );

    }



    @Override
    public void deleteInspection(UUID id) {


        MissionVehicleInspection inspection =
                inspectionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Inspection not found")
                        );


        inspectionRepository.delete(inspection);

    }

}