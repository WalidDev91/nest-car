package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.mission.CreateMissionRequest;
import com.mvpnest.fleetmanagement.dto.mission.MissionDTO;
import com.mvpnest.fleetmanagement.dto.mission.UpdateMissionRequest;
import com.mvpnest.fleetmanagement.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    public MissionDTO create(
            @RequestBody CreateMissionRequest request
    ) {
        return missionService.createMission(request);
    }

    @GetMapping("/{id}")
    public MissionDTO getById(
            @PathVariable UUID id
    ) {
        return missionService.getMissionById(id);
    }

    @GetMapping
    public List<MissionDTO> getAll() {
        return missionService.getAllMissions();
    }

    @PutMapping("/{id}")
    public MissionDTO update(
            @PathVariable UUID id,
            @RequestBody UpdateMissionRequest request
    ) {
        return missionService.updateMission(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id
    ) {
        missionService.deleteMission(id);
    }
}