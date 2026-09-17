package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.mission.MissionDTO;
import com.mvpnest.fleetmanagement.entity.Mission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MissionVehicleInspectionMapper.class)

public interface MissionMapper {

    @Mapping(target = "driverId", expression = "java(mission.getDriver() != null ? mission.getDriver().getId() : null)")
    @Mapping(target = "driverName", expression = "java(mission.getDriver() != null ? mission.getDriver().getFirstName() + \" \" + mission.getDriver().getLastName() : null)")
    @Mapping(target = "vehicleId", expression = "java(mission.getVehicle() != null ? mission.getVehicle().getId() : null)")
    @Mapping(target = "vehiclePlateNumber", expression = "java(mission.getVehicle() != null ? mission.getVehicle().getPlateNumber() : null)")
    @Mapping(source = "departureLocation", target = "departureLocation")
    @Mapping(source = "destinationLocation", target = "destinationLocation")
    @Mapping(source = "vehicleInspections", target = "vehicleInspections")
    @Mapping(source = "missionDocuments", target = "documents")
    MissionDTO toDTO(Mission mission);

}