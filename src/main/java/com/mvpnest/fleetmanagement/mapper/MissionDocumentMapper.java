package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.entity.MissionDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MissionDocumentMapper {

    @Mapping(source = "mission.id", target = "missionId")
    @Mapping(source = "mission.title", target = "missionTitle")
    @Mapping(source = "uploadedBy.id", target = "uploadedById")
    @Mapping(expression = "java(document.getUploadedBy() != null ? document.getUploadedBy().getFirstName() + \" \" + document.getUploadedBy().getLastName() : null)", target = "uploadedByName")
    MissionDocumentDTO toDTO(MissionDocument document);
}