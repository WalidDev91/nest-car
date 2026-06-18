package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.DriverDocumentDTO;

import java.util.List;
import java.util.UUID;

public interface DriverDocumentService {

    DriverDocumentDTO createDocument(DriverDocumentDTO dto);

    DriverDocumentDTO getDocumentById(UUID id);

    List<DriverDocumentDTO> getAllDocuments();

    List<DriverDocumentDTO> getDocumentsByDriverId(UUID driverId);

    DriverDocumentDTO updateDocument(UUID id, DriverDocumentDTO dto);

    void deleteDocument(UUID id);
}
