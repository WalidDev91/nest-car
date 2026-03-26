package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.entity.MissionVehiclePhoto;
import com.mvpnest.fleetmanagement.service.MissionVehiclePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class MissionVehiclePhotoController {

    private final MissionVehiclePhotoService service;

    @PostMapping
    public MissionVehiclePhoto create(@RequestBody MissionVehiclePhoto photo) {
        return service.createPhoto(photo);
    }

    @GetMapping("/{id}")
    public MissionVehiclePhoto getById(@PathVariable UUID id) {
        return service.getPhotoById(id);
    }

    @GetMapping
    public List<MissionVehiclePhoto> getAll() {
        return service.getAllPhotos();
    }

    @GetMapping("/inspection/{inspectionId}")
    public List<MissionVehiclePhoto> getByInspection(@PathVariable UUID inspectionId) {
        return service.getPhotosByInspectionId(inspectionId);
    }

    @PutMapping("/{id}")
    public MissionVehiclePhoto update(@PathVariable UUID id, @RequestBody MissionVehiclePhoto photo) {
        return service.updatePhoto(id, photo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deletePhoto(id);
    }
}
