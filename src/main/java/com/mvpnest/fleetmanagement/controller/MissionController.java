package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.mission.*;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import com.mvpnest.fleetmanagement.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    public MissionDTO create(@RequestBody CreateMissionRequest request) {
        return missionService.createMission(request);
    }

    @GetMapping("/{id}")
    public MissionDTO getById(@PathVariable UUID id) {
        return missionService.getMissionById(id);
    }

    @GetMapping
    public List<MissionDTO> getAll() {
        return missionService.getAllMissions();
    }

    @PutMapping("/{id}")
    public MissionDTO update(@PathVariable UUID id, @RequestBody UpdateMissionRequest request) {
        return missionService.updateMission(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        missionService.deleteMission(id);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<MissionDTO> getByVehicle(@PathVariable UUID vehicleId) {
        return missionService.getMissionsByVehicleId(vehicleId);
    }

    @GetMapping("/status/{status}")
    public List<MissionDTO> getByStatus(@PathVariable MissionStatus status) {
        return missionService.getMissionsByStatus(status);
    }

    @PatchMapping("/{id}/assignment")
    public MissionDTO assignMission(@PathVariable UUID id, @RequestBody MissionAssignmentRequest request) {
        return missionService.assignMission(id, request);
    }

    @PatchMapping("/{id}/verification")
    public MissionDTO updateDocumentsVerification(@PathVariable UUID id, @RequestParam(required = false) Boolean verified) {
        return missionService.updateDocumentsVerification(id, verified);
    }

    @PostMapping("/{id}/inspection")
    public MissionDTO saveInspection(@PathVariable UUID id, @RequestBody MissionInspectionRequest request) {
        return missionService.saveInspection(id, request);
    }

    @PostMapping("/{id}/inspection/photos")
    public MissionDTO uploadInspectionPhoto(@PathVariable UUID id, @RequestPart("file") MultipartFile file, @RequestParam(required = false) String description) {
        return missionService.uploadInspectionPhoto(id, file, description);
    }

    @DeleteMapping("/{id}/inspection")
    public MissionDTO deleteInspection(@PathVariable UUID id) {
        return missionService.deleteInspection(id);
    }

    @DeleteMapping("/{id}/inspection/photos/{photoId}")
    public MissionDTO deleteInspectionPhoto(@PathVariable UUID id, @PathVariable UUID photoId) {
        return missionService.deleteInspectionPhoto(id, photoId);
    }
}