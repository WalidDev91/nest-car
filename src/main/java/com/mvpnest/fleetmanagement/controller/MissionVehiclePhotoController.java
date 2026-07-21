package com.mvpnest.fleetmanagement.controller;


import com.mvpnest.fleetmanagement.dto.missionvehiclephoto.MissionVehiclePhotoDTO;
import com.mvpnest.fleetmanagement.dto.missionvehiclephoto.UpdateMissionVehiclePhotoRequest;
import com.mvpnest.fleetmanagement.service.MissionVehiclePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/api/mission-photos")
@RequiredArgsConstructor
public class MissionVehiclePhotoController {



    private final MissionVehiclePhotoService service;





    @GetMapping("/{id}")
    public MissionVehiclePhotoDTO getById(
            @PathVariable UUID id
    ){

        return service.getPhotoById(id);

    }





    @GetMapping
    public List<MissionVehiclePhotoDTO> getAll(){

        return service.getAllPhotos();

    }





    @GetMapping("/inspection/{inspectionId}")
    public List<MissionVehiclePhotoDTO> getByInspection(
            @PathVariable UUID inspectionId
    ){

        return service.getPhotosByInspectionId(inspectionId);

    }





    @PostMapping("/upload")
    public MissionVehiclePhotoDTO upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam UUID inspectionId,
            @RequestParam(required = false) String description
    ){

        return service.uploadPhoto(
                file,
                inspectionId,
                description
        );

    }





    @PutMapping("/{id}")
    public MissionVehiclePhotoDTO update(
            @PathVariable UUID id,
            @RequestBody UpdateMissionVehiclePhotoRequest request
    ){

        return service.updatePhoto(id, request);

    }





    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id
    ){

        service.deletePhoto(id);

    }

}