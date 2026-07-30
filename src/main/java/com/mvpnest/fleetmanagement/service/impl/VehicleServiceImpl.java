package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.vehicle.CreateVehicleRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.UpdateVehicleRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.mapper.VehicleMapper;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final VehicleMapper vehicleMapper;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public VehicleDTO createVehicle(CreateVehicleRequest request) {

        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Vehicle vehicle = Vehicle.builder().plateNumber(request.getPlateNumber()).brand(request.getBrand()).model(request.getModel()).year(request.getYear()).admin(admin).build();

        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleDTO getVehicleById(UUID id) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        return vehicleMapper.toDTO(vehicle);
    }

    @Override
    public List<VehicleDTO> getVehiclesByAdmin(UUID adminId) {

        return vehicleRepository.findByAdminId(adminId).stream().map(vehicleMapper::toDTO).toList();
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream().map(vehicleMapper::toDTO).toList();
    }

    @Override
    public VehicleDTO updateVehicle(UUID id, UpdateVehicleRequest request) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        vehicle.setPlateNumber(request.getPlateNumber());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setAdmin(admin);

        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public void deleteVehicle(UUID id) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicleRepository.delete(vehicle);
    }

    @Override
    public VehicleDTO uploadImage(UUID id, MultipartFile file) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        try {

            Path folder = Paths.get(uploadDir, "vehicles");
            Files.createDirectories(folder);

            // Delete old image if it exists
            if (vehicle.getImageUrl() != null) {
                Files.deleteIfExists(folder.resolve(vehicle.getImageUrl()));
            }

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Files.copy(file.getInputStream(), folder.resolve(filename));

            vehicle.setImageUrl(filename);

            vehicleRepository.save(vehicle);

            return vehicleMapper.toDTO(vehicle);

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed");
        }

    }

    @Override
    public VehicleDTO deleteImage(UUID id) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        try {

            if (vehicle.getImageUrl() != null) {

                Path folder = Paths.get(uploadDir, "vehicles");

                Files.deleteIfExists(folder.resolve(vehicle.getImageUrl()));

                vehicle.setImageUrl(null);

                vehicleRepository.save(vehicle);

            }

            return vehicleMapper.toDTO(vehicle);

        } catch (IOException e) {
            throw new RuntimeException("Image deletion failed");
        }

    }
}