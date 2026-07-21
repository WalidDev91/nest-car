package com.mvpnest.fleetmanagement.service;


import com.mvpnest.fleetmanagement.dto.missionvehiclephoto.MissionVehiclePhotoDTO;
import com.mvpnest.fleetmanagement.dto.missionvehiclephoto.UpdateMissionVehiclePhotoRequest;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;


public interface MissionVehiclePhotoService {


    MissionVehiclePhotoDTO uploadPhoto(MultipartFile file, UUID inspectionId, String description);

    MissionVehiclePhotoDTO getPhotoById(UUID id);

    List<MissionVehiclePhotoDTO> getAllPhotos();

    List<MissionVehiclePhotoDTO> getPhotosByInspectionId(UUID inspectionId);

    MissionVehiclePhotoDTO updatePhoto(UUID id, UpdateMissionVehiclePhotoRequest request);

    void deletePhoto(UUID id);

}