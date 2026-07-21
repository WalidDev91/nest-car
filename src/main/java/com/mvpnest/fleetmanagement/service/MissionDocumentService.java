package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.UpdateMissionDocumentRequest;
import com.mvpnest.fleetmanagement.dto.missiondocument.UploadMissionDocumentRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MissionDocumentService {


    MissionDocumentDTO getDocumentById(UUID id);

    List<MissionDocumentDTO> getAllDocuments();

    List<MissionDocumentDTO> getDocumentsByMissionId(UUID missionId);

    MissionDocumentDTO uploadDocument(MultipartFile file, UploadMissionDocumentRequest request);

    MissionDocumentDTO updateDocument(UUID id, UpdateMissionDocumentRequest request);

    void deleteDocument(UUID id);

    ResponseEntity<Resource> downloadDocument(UUID id);

}
