package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleDocumentMapper {

    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.plateNumber", target = "vehiclePlateNumber")
    @Mapping(source = "expiryDate", target = "expiryDate")
    @Mapping(source = "uploadedBy.id", target = "uploadedById")
    @Mapping(expression = "java(document.getUploadedBy() != null ? document.getUploadedBy().getFirstName() + \" \" + document.getUploadedBy().getLastName() : null)", target = "uploadedByName")
    VehicleDocumentDTO toDTO(VehicleDocument document);

}