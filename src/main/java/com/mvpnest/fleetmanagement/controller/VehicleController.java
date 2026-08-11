package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.vehicle.CreateVehicleRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.UpdateVehicleRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;


    // ==========================================================
    // CREATE
    // ==========================================================

    @PostMapping
    public VehicleDTO create(@RequestBody CreateVehicleRequest request) {

        return vehicleService.createVehicle(request);

    }


    // ==========================================================
    // GET ONE
    // ==========================================================

    @GetMapping("/{id}")
    public VehicleDTO getById(@PathVariable UUID id) {

        return vehicleService.getVehicleById(id);

    }


    // ==========================================================
    // GET ALL
    // ==========================================================

    @GetMapping
    public List<VehicleDTO> getAll(@RequestParam(required = false) UUID adminId) {

        if (adminId != null) {

            return vehicleService.getVehiclesByAdmin(adminId);

        }

        return vehicleService.getAllVehicles();

    }


    // ==========================================================
    // UPDATE
    // ==========================================================

    @PutMapping("/{id}")
    public VehicleDTO update(@PathVariable UUID id, @RequestBody UpdateVehicleRequest request) {

        return vehicleService.updateVehicle(id, request);

    }


    // ==========================================================
    // DELETE
    // ==========================================================

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {

        vehicleService.deleteVehicle(id);

    }


    // ==========================================================
    // UPLOAD VEHICLE PHOTO
    // ==========================================================

    @PostMapping("/{id}/photos")
    public VehicleDTO uploadPhoto(@PathVariable UUID id, @RequestParam("file") MultipartFile file, @RequestParam(required = false) String description) {
        return vehicleService.uploadPhoto(id, file, description);
    }


    // ==========================================================
    // DELETE VEHICLE PHOTO
    // ==========================================================

    @DeleteMapping("/{id}/photos/{photoId}")
    public VehicleDTO deletePhoto(@PathVariable UUID id, @PathVariable UUID photoId) {
        return vehicleService.deletePhoto(id, photoId);
    }

}