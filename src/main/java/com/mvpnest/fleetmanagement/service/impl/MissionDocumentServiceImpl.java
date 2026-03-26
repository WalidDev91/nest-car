package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.entity.MissionDocument;
import com.mvpnest.fleetmanagement.repository.MissionDocumentRepository;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionDocumentServiceImpl implements MissionDocumentService {

    private final MissionDocumentRepository repository;

    @Override
    public MissionDocument createDocument(MissionDocument document) {
        return repository.save(document);
    }

    @Override
    public MissionDocument getDocumentById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MissionDocument not found with id: " + id));
    }

    @Override
    public List<MissionDocument> getAllDocuments() {
        return repository.findAll();
    }

    @Override
    public List<MissionDocument> getDocumentsByMissionId(UUID missionId) {
        return repository.findByMissionId(missionId);
    }

    @Override
    public MissionDocument updateDocument(UUID id, MissionDocument document) {
        MissionDocument existing = getDocumentById(id);

        existing.setTitle(document.getTitle());
        existing.setFileUrl(document.getFileUrl());
        existing.setMission(document.getMission());

        return repository.save(existing);
    }

    @Override
    public void deleteDocument(UUID id) {
        MissionDocument document = getDocumentById(id);
        repository.delete(document);
    }
}
