package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.mission.MissionDTO;
import com.mvpnest.fleetmanagement.entity.Mission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MissionMapper {

    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(expression = "java(mission.getDriver().getFirstName() + \" \" + mission.getDriver().getLastName())", target = "driverName")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.plateNumber", target = "vehiclePlateNumber")
    MissionDTO toDTO(Mission mission);
}