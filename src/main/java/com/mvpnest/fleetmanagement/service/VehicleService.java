package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.vehicle.VehicleCreateRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface VehicleService {

    VehicleDTO createVehicle(VehicleCreateRequest request);

    VehicleDTO getVehicleById(UUID id);

    List<VehicleDTO> getAllVehicles();

    VehicleDTO updateVehicle(UUID id, VehicleUpdateRequest request);

    void deleteVehicle(UUID id);
}