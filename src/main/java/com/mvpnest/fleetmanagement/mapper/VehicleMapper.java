package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(source = "admin.id", target = "adminId")
    @Mapping(source = "admin.firstName", target = "adminName")
    @Mapping(source = "photos", target = "photos")
    VehicleDTO toDTO(Vehicle vehicle);
}