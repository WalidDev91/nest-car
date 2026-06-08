package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleCreateRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleUpdateRequest;
import com.mvpnest.fleetmanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public VehicleDTO create(@RequestBody VehicleCreateRequest request) {
        return vehicleService.createVehicle(request);
    }

    @GetMapping("/{id}")
    public VehicleDTO getById(@PathVariable UUID id) {
        return vehicleService.getVehicleById(id);
    }

    @GetMapping
    public List<VehicleDTO> getAll() {
        return vehicleService.getAllVehicles();
    }

    @PutMapping("/{id}")
    public VehicleDTO update(@PathVariable UUID id,
                             @RequestBody VehicleUpdateRequest request) {
        return vehicleService.updateVehicle(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        vehicleService.deleteVehicle(id);
    }
}