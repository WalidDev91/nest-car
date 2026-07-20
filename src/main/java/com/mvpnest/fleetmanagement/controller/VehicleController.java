package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleCreateRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleUpdateRequest;
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
    public VehicleDTO create(@RequestBody VehicleCreateRequest request) {

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
    public List<VehicleDTO> getAll(
            @RequestParam(required = false) UUID adminId
    ) {

        if (adminId != null) {

            return vehicleService.getVehiclesByAdmin(adminId);

        }

        return vehicleService.getAllVehicles();

    }


    // ==========================================================
    // UPDATE
    // ==========================================================

    @PutMapping("/{id}")
    public VehicleDTO update(
            @PathVariable UUID id,
            @RequestBody VehicleUpdateRequest request
    ) {

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
    // UPLOAD IMAGE
    // ==========================================================

    @PostMapping("/{id}/image")
    public VehicleDTO uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) {

        return vehicleService.uploadImage(id, file);

    }


    // ==========================================================
    // DELETE IMAGE
    // ==========================================================

    @DeleteMapping("/{id}/image")
    public VehicleDTO deleteImage(@PathVariable UUID id) {

        return vehicleService.deleteImage(id);

    }

}