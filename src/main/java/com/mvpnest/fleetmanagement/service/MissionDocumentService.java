package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.MissionDocument;

import java.util.List;
import java.util.UUID;

public interface MissionDocumentService {

    MissionDocument createDocument(MissionDocument document);

    MissionDocument getDocumentById(UUID id);

    List<MissionDocument> getAllDocuments();

    List<MissionDocument> getDocumentsByMissionId(UUID missionId);

    MissionDocument updateDocument(UUID id, MissionDocument document);

    void deleteDocument(UUID id);
}
