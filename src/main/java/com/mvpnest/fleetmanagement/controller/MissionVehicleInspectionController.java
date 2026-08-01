package com.mvpnest.fleetmanagement.controller;


import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.service.MissionVehicleInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/inspections")
@RequiredArgsConstructor
public class MissionVehicleInspectionController {


    private final MissionVehicleInspectionService service;


    @GetMapping("/{id}")
    public MissionVehicleInspectionDTO getById(@PathVariable UUID id) {

        return service.getInspectionById(id);

    }


    @GetMapping
    public List<MissionVehicleInspectionDTO> getAll() {

        return service.getAllInspections();

    }


    @GetMapping("/mission/{missionId}")
    public MissionVehicleInspectionDTO getByMission(@PathVariable UUID missionId) {

        return service.getByMissionId(missionId);

    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {

        service.deleteInspection(id);

    }

}