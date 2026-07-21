package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.missionvehiclephoto.MissionVehiclePhotoDTO;
import com.mvpnest.fleetmanagement.entity.MissionVehiclePhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface MissionVehiclePhotoMapper {

    @Mapping(source = "inspection.id", target = "inspectionId")
    MissionVehiclePhotoDTO toDTO(MissionVehiclePhoto photo);

}