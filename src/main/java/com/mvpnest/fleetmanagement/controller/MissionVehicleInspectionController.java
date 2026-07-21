package com.mvpnest.fleetmanagement.controller;


import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.CreateMissionVehicleInspectionRequest;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.UpdateMissionVehicleInspectionRequest;
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
    public MissionVehicleInspectionDTO create(
            @RequestBody CreateMissionVehicleInspectionRequest request
    ){

        return service.createInspection(request);

    }





    @GetMapping("/{id}")
    public MissionVehicleInspectionDTO getById(
            @PathVariable UUID id
    ){

        return service.getInspectionById(id);

    }





    @GetMapping
    public List<MissionVehicleInspectionDTO> getAll(){

        return service.getAllInspections();

    }





    @GetMapping("/mission/{missionId}")
    public MissionVehicleInspectionDTO getByMission(
            @PathVariable UUID missionId
    ){

        return service.getByMissionId(missionId);

    }





    @PutMapping("/{id}")
    public MissionVehicleInspectionDTO update(
            @PathVariable UUID id,
            @RequestBody UpdateMissionVehicleInspectionRequest request
    ){

        return service.updateInspection(id, request);

    }





    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id
    ){

        service.deleteInspection(id);

    }

}