package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.vehicle.CreateVehicleRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.UpdateVehicleRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.entity.VehiclePhoto;
import com.mvpnest.fleetmanagement.exception.ResourceNotFoundException;
import com.mvpnest.fleetmanagement.mapper.VehicleMapper;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.repository.VehiclePhotoRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.security.SecurityUtils;
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

    private static final String VEHICLE_NOT_FOUND = "Vehicle not found";
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final VehicleMapper vehicleMapper;
    private final VehiclePhotoRepository vehiclePhotoRepository;
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public VehicleDTO createVehicle(CreateVehicleRequest request) {

        User creator = SecurityUtils.getCurrentUser();

        Vehicle vehicle = Vehicle.builder().plateNumber(request.getPlateNumber()).brand(request.getBrand()).model(request.getModel()).year(request.getYear()).admin(creator).build();

        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));

    }

    @Override
    public VehicleDTO getVehicleById(UUID id) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND));

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

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException(VEHICLE_NOT_FOUND));

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

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException(VEHICLE_NOT_FOUND));

        vehicleRepository.delete(vehicle);
    }

    @Override
    public VehicleDTO uploadPhoto(UUID id, MultipartFile file, String description) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException(VEHICLE_NOT_FOUND));

        try {

            Path folder = Paths.get(uploadDir, "vehicles");
            Files.createDirectories(folder);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Files.copy(file.getInputStream(), folder.resolve(filename));

            VehiclePhoto photo = VehiclePhoto.builder().photoUrl(filename).description(description).vehicle(vehicle).build();

            vehiclePhotoRepository.save(photo);

            return vehicleMapper.toDTO(vehicle);

        } catch (IOException e) {
            throw new RuntimeException("Photo upload failed");
        }
    }

    @Override
    public VehicleDTO deletePhoto(UUID id, UUID photoId) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException(VEHICLE_NOT_FOUND));

        VehiclePhoto photo = vehiclePhotoRepository.findById(photoId).orElseThrow(() -> new RuntimeException("Vehicle photo not found"));

        if (!photo.getVehicle().getId().equals(vehicle.getId())) {
            throw new RuntimeException("Photo does not belong to this vehicle");
        }

        try {

            Path folder = Paths.get(uploadDir, "vehicles");

            Files.deleteIfExists(folder.resolve(photo.getPhotoUrl()));

            vehiclePhotoRepository.delete(photo);

            return vehicleMapper.toDTO(vehicle);

        } catch (IOException e) {
            throw new RuntimeException("Photo deletion failed");
        }
    }
}