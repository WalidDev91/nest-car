package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.UploadMissionDocumentRequest;
import com.mvpnest.fleetmanagement.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MissionDocumentService {


    MissionDocumentDTO getDocumentById(UUID id);

    List<MissionDocumentDTO> getAllDocuments(User currentUser);

    List<MissionDocumentDTO> getDocumentsByMissionId(UUID missionId);

    MissionDocumentDTO uploadDocument(MultipartFile file, UploadMissionDocumentRequest request);

    MissionDocumentDTO updateDocumentTitle(UUID id, String title);

    void deleteDocument(UUID id);

    ResponseEntity<Resource> downloadDocument(UUID id);

}
