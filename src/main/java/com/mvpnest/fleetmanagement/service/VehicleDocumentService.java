package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.VehicleDocument;

import java.util.List;
import java.util.UUID;

public interface VehicleDocumentService {

    VehicleDocument createDocument(VehicleDocument document);

    VehicleDocument getDocumentById(UUID id);

    List<VehicleDocument> getAllDocuments();

    List<VehicleDocument> getDocumentsByVehicleId(UUID vehicleId);

    VehicleDocument updateDocument(UUID id, VehicleDocument document);

    void deleteDocument(UUID id);
}
