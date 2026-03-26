package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.entity.Mission;
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
    public Mission create(@RequestBody Mission mission) {
        return missionService.createMission(mission);
    }

    @GetMapping("/{id}")
    public Mission getById(@PathVariable UUID id) {
        return missionService.getMissionById(id);
    }

    @GetMapping
    public List<Mission> getAll() {
        return missionService.getAllMissions();
    }

    @PutMapping("/{id}")
    public Mission update(@PathVariable UUID id, @RequestBody Mission mission) {
        return missionService.updateMission(id, mission);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        missionService.deleteMission(id);
    }
}
