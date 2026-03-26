package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.DriverDocument;

import java.util.List;
import java.util.UUID;

public interface DriverDocumentService {

    DriverDocument createDocument(DriverDocument document);

    DriverDocument getDocumentById(UUID id);

    List<DriverDocument> getAllDocuments();

    List<DriverDocument> getDocumentsByDriverId(UUID driverId);

    DriverDocument updateDocument(UUID id, DriverDocument document);

    void deleteDocument(UUID id);
}
