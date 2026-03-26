package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicleById(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle updateVehicle(UUID id, Vehicle vehicle) {
        Vehicle existing = getVehicleById(id);

        existing.setPlateNumber(vehicle.getPlateNumber());
        existing.setBrand(vehicle.getBrand());
        existing.setModel(vehicle.getModel());
        existing.setYear(vehicle.getYear());
        existing.setAdmin(vehicle.getAdmin());

        return vehicleRepository.save(existing);
    }

    @Override
    public void deleteVehicle(UUID id) {
        Vehicle vehicle = getVehicleById(id);
        vehicleRepository.delete(vehicle);
    }
}
