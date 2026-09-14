package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MissionVehicleInspectionMapper {

    @Mapping(target = "photoIds", expression = "java(inspection.getPhotos() != null ? inspection.getPhotos().stream().map(photo -> photo.getId()).collect(java.util.stream.Collectors.toList()) : java.util.List.of())")
    MissionVehicleInspectionDTO toDTO(MissionVehicleInspection inspection);

}