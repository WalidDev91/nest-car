package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.entity.Vehicle;
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
    public Vehicle create(@RequestBody Vehicle vehicle) {
        return vehicleService.createVehicle(vehicle);
    }

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable UUID id) {
        return vehicleService.getVehicleById(id);
    }

    @GetMapping
    public List<Vehicle> getAll() {
        return vehicleService.getAllVehicles();
    }

    @PutMapping("/{id}")
    public Vehicle update(@PathVariable UUID id, @RequestBody Vehicle vehicle) {
        return vehicleService.updateVehicle(id, vehicle);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        vehicleService.deleteVehicle(id);
    }
}
