package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.MissionDocumentDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MissionDocumentService {

    MissionDocumentDTO createDocument(MissionDocumentDTO dto);

    MissionDocumentDTO getDocumentById(UUID id);

    List<MissionDocumentDTO> getAllDocuments();

    List<MissionDocumentDTO> getDocumentsByMissionId(UUID missionId);

    MissionDocumentDTO updateDocument(UUID id, MissionDocumentDTO dto);

    void deleteDocument(UUID id);

    MissionDocumentDTO uploadDocument(MultipartFile file, String title, UUID missionId);

    ResponseEntity<Resource> downloadDocument(UUID id);
}
