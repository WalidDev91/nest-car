package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.entity.DriverDocument;
import com.mvpnest.fleetmanagement.repository.DriverDocumentRepository;
import com.mvpnest.fleetmanagement.service.DriverDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverDocumentServiceImpl implements DriverDocumentService {

    private final DriverDocumentRepository documentRepository;

    @Override
    public DriverDocument createDocument(DriverDocument document) {
        return documentRepository.save(document);
    }

    @Override
    public DriverDocument getDocumentById(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DriverDocument not found with id: " + id));
    }

    @Override
    public List<DriverDocument> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public List<DriverDocument> getDocumentsByDriverId(UUID driverId) {
        return documentRepository.findByDriverId(driverId);
    }

    @Override
    public DriverDocument updateDocument(UUID id, DriverDocument document) {
        DriverDocument existing = getDocumentById(id);

        existing.setTitle(document.getTitle());
        existing.setType(document.getType());
        existing.setFileUrl(document.getFileUrl());
        existing.setStatus(document.getStatus());
        existing.setUploadedAt(document.getUploadedAt());
        existing.setValidatedAt(document.getValidatedAt());
        existing.setDriver(document.getDriver());

        return documentRepository.save(existing);
    }

    @Override
    public void deleteDocument(UUID id) {
        DriverDocument document = getDocumentById(id);
        documentRepository.delete(document);
    }
}
