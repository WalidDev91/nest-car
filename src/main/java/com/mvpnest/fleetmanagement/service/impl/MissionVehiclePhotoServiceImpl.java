package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.entity.MissionVehiclePhoto;
import com.mvpnest.fleetmanagement.repository.MissionVehiclePhotoRepository;
import com.mvpnest.fleetmanagement.service.MissionVehiclePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionVehiclePhotoServiceImpl implements MissionVehiclePhotoService {

    private final MissionVehiclePhotoRepository repository;

    @Override
    public MissionVehiclePhoto createPhoto(MissionVehiclePhoto photo) {
        return repository.save(photo);
    }

    @Override
    public MissionVehiclePhoto getPhotoById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + id));
    }

    @Override
    public List<MissionVehiclePhoto> getAllPhotos() {
        return repository.findAll();
    }

    @Override
    public List<MissionVehiclePhoto> getPhotosByInspectionId(UUID inspectionId) {
        return repository.findByInspectionId(inspectionId);
    }

    @Override
    public MissionVehiclePhoto updatePhoto(UUID id, MissionVehiclePhoto photo) {
        MissionVehiclePhoto existing = getPhotoById(id);

        existing.setPhotoUrl(photo.getPhotoUrl());
        existing.setDescription(photo.getDescription());
        existing.setTakenAt(photo.getTakenAt());
        existing.setInspection(photo.getInspection());

        return repository.save(existing);
    }

    @Override
    public void deletePhoto(UUID id) {
        MissionVehiclePhoto photo = getPhotoById(id);
        repository.delete(photo);
    }
}
