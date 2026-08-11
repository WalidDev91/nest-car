package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.vehicle.CreateVehicleRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.dto.vehicle.UpdateVehicleRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface VehicleService {

    VehicleDTO createVehicle(CreateVehicleRequest request);

    VehicleDTO getVehicleById(UUID id);

    List<VehicleDTO> getAllVehicles();

    VehicleDTO updateVehicle(UUID id, UpdateVehicleRequest request);

    void deleteVehicle(UUID id);

    List<VehicleDTO> getVehiclesByAdmin(UUID adminId);

    VehicleDTO uploadPhoto(UUID id, MultipartFile file, String description);

    VehicleDTO deletePhoto(UUID id, UUID photoId);
}