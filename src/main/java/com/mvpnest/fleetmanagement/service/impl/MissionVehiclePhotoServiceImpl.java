package com.mvpnest.fleetmanagement.service.impl;


import com.mvpnest.fleetmanagement.dto.missionvehiclephoto.MissionVehiclePhotoDTO;
import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import com.mvpnest.fleetmanagement.entity.MissionVehiclePhoto;
import com.mvpnest.fleetmanagement.mapper.MissionVehiclePhotoMapper;
import com.mvpnest.fleetmanagement.repository.MissionVehicleInspectionRepository;
import com.mvpnest.fleetmanagement.repository.MissionVehiclePhotoRepository;
import com.mvpnest.fleetmanagement.service.MissionVehiclePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MissionVehiclePhotoServiceImpl implements MissionVehiclePhotoService {


    private final MissionVehiclePhotoRepository photoRepository;

    private final MissionVehicleInspectionRepository inspectionRepository;

    private final MissionVehiclePhotoMapper mapper;


    @Value("${app.upload.dir}")
    private String uploadDir;


    @Override
    public MissionVehiclePhotoDTO uploadPhoto(MultipartFile file, UUID inspectionId, String description) {


        try {


            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }


            Path uploadPath = Paths.get(uploadDir, "mission-vehicle-photos");


            Files.createDirectories(uploadPath);


            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();


            Path filePath = uploadPath.resolve(filename);


            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


            MissionVehicleInspection inspection = inspectionRepository.findById(inspectionId).orElseThrow(() -> new RuntimeException("Inspection not found"));


            MissionVehiclePhoto photo = MissionVehiclePhoto.builder().photoUrl("/uploads/mission-vehicle-photos/" + filename).description(description).takenAt(LocalDateTime.now()).inspection(inspection).build();

            return mapper.toDTO(photoRepository.save(photo));


        } catch (IOException e) {

            throw new RuntimeException("Upload failed: " + e.getMessage());

        }

    }


    @Override
    public MissionVehiclePhotoDTO getPhotoById(UUID id) {


        return mapper.toDTO(photoRepository.findById(id).orElseThrow(() -> new RuntimeException("Photo not found")));

    }


    @Override
    public List<MissionVehiclePhotoDTO> getAllPhotos() {


        return photoRepository.findAll().stream().map(mapper::toDTO).toList();

    }


    @Override
    public List<MissionVehiclePhotoDTO> getPhotosByInspectionId(UUID inspectionId) {


        return photoRepository.findByInspectionId(inspectionId).stream().map(mapper::toDTO).toList();

    }


    @Override
    public void deletePhoto(UUID id) {


        MissionVehiclePhoto photo = photoRepository.findById(id).orElseThrow(() -> new RuntimeException("Photo not found"));


        photoRepository.delete(photo);

    }

}