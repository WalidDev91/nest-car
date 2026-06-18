package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleDocumentMapper {

    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.plateNumber", target = "vehiclePlateNumber")
    VehicleDocumentDTO toDTO(VehicleDocument document);

    VehicleDocument toEntity(VehicleDocumentDTO dto);
}