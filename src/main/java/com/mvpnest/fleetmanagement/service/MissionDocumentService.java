package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.MissionDocumentDTO;

import java.util.List;
import java.util.UUID;

public interface MissionDocumentService {

    MissionDocumentDTO createDocument(MissionDocumentDTO dto);

    MissionDocumentDTO getDocumentById(UUID id);

    List<MissionDocumentDTO> getAllDocuments();

    List<MissionDocumentDTO> getDocumentsByMissionId(UUID missionId);

    MissionDocumentDTO updateDocument(UUID id, MissionDocumentDTO dto);

    void deleteDocument(UUID id);
}
