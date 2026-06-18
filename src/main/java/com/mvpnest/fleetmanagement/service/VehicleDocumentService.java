package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.VehicleDocumentDTO;

import java.util.List;
import java.util.UUID;

public interface VehicleDocumentService {

    VehicleDocumentDTO createDocument(VehicleDocumentDTO dto);

    VehicleDocumentDTO getDocumentById(UUID id);

    List<VehicleDocumentDTO> getAllDocuments();

    List<VehicleDocumentDTO> getDocumentsByVehicleId(UUID vehicleId);

    VehicleDocumentDTO updateDocument(UUID id, VehicleDocumentDTO dto);

    void deleteDocument(UUID id);
}
