package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface MissionVehicleInspectionMapper {

    @Mapping(source = "mission.id", target = "missionId")
    @Mapping(source = "mission.title", target = "missionTitle")
    MissionVehicleInspectionDTO toDTO(MissionVehicleInspection inspection);

}