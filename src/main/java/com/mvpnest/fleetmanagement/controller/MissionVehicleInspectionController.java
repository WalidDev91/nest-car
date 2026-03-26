package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
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

    @PostMapping
    public MissionVehicleInspection create(@RequestBody MissionVehicleInspection inspection) {
        return service.createInspection(inspection);
    }

    @GetMapping("/{id}")
    public MissionVehicleInspection getById(@PathVariable UUID id) {
        return service.getInspectionById(id);
    }

    @GetMapping
    public List<MissionVehicleInspection> getAll() {
        return service.getAllInspections();
    }

    @GetMapping("/mission/{missionId}")
    public MissionVehicleInspection getByMission(@PathVariable UUID missionId) {
        return service.getByMissionId(missionId);
    }

    @PutMapping("/{id}")
    public MissionVehicleInspection update(@PathVariable UUID id,
                                           @RequestBody MissionVehicleInspection inspection) {
        return service.updateInspection(id, inspection);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteInspection(id);
    }
}
