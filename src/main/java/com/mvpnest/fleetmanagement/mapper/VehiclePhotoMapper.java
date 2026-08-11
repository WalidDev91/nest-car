package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.vehicle.VehiclePhotoDTO;
import com.mvpnest.fleetmanagement.entity.VehiclePhoto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehiclePhotoMapper {

    VehiclePhotoDTO toDTO(VehiclePhoto photo);
}
