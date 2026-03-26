package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import com.mvpnest.fleetmanagement.repository.VehicleDocumentRepository;
import com.mvpnest.fleetmanagement.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleDocumentServiceImpl implements VehicleDocumentService {

    private final VehicleDocumentRepository repository;

    @Override
    public VehicleDocument createDocument(VehicleDocument document) {
        return repository.save(document);
    }

    @Override
    public VehicleDocument getDocumentById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VehicleDocument not found with id: " + id));
    }

    @Override
    public List<VehicleDocument> getAllDocuments() {
        return repository.findAll();
    }

    @Override
    public List<VehicleDocument> getDocumentsByVehicleId(UUID vehicleId) {
        return repository.findByVehicleId(vehicleId);
    }

    @Override
    public VehicleDocument updateDocument(UUID id, VehicleDocument document) {
        VehicleDocument existing = getDocumentById(id);

        existing.setTitle(document.getTitle());
        existing.setType(document.getType());
        existing.setFileUrl(document.getFileUrl());
        existing.setYear(document.getYear());
        existing.setVehicle(document.getVehicle());

        return repository.save(existing);
    }

    @Override
    public void deleteDocument(UUID id) {
        VehicleDocument document = getDocumentById(id);
        repository.delete(document);
    }
}
