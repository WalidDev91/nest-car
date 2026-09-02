package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.UpdateMissionDocumentRequest;
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

    MissionDocumentDTO uploadDocument(MultipartFile file, UploadMissionDocumentRequest request, User currentUser);

    //MissionDocumentDTO updateDocumentTitle(UUID id, String title);

    ResponseEntity<Resource> downloadDocument(UUID id);

    Resource preview(UUID id);

    MissionDocumentDTO updateDocument(UUID id, UpdateMissionDocumentRequest request);

    void deleteDocument(UUID id);

}
