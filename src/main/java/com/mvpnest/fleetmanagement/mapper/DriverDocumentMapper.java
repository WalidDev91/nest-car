package com.mvpnest.fleetmanagement.mapper;


import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.entity.DriverDocument;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface DriverDocumentMapper {

    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(expression = "java(document.getDriver() != null ? document.getDriver().getFirstName() + \" \" + document.getDriver().getLastName() : null)", target = "driverName")
    DriverDocumentDTO toDTO(DriverDocument document);

}