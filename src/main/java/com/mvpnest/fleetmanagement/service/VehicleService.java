package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.Vehicle;

import java.util.List;
import java.util.UUID;

public interface VehicleService {

    Vehicle createVehicle(Vehicle vehicle);

    Vehicle getVehicleById(UUID id);

    List<Vehicle> getAllVehicles();

    Vehicle updateVehicle(UUID id, Vehicle vehicle);

    void deleteVehicle(UUID id);
}
