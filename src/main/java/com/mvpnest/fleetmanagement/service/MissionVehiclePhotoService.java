package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.MissionVehiclePhoto;

import java.util.List;
import java.util.UUID;

public interface MissionVehiclePhotoService {

    MissionVehiclePhoto createPhoto(MissionVehiclePhoto photo);

    MissionVehiclePhoto getPhotoById(UUID id);

    List<MissionVehiclePhoto> getAllPhotos();

    List<MissionVehiclePhoto> getPhotosByInspectionId(UUID inspectionId);

    MissionVehiclePhoto updatePhoto(UUID id, MissionVehiclePhoto photo);

    void deletePhoto(UUID id);

}
