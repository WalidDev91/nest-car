package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.enums.VehicleType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface VehicleDocumentService {

    VehicleDocumentDTO createDocument(VehicleDocumentDTO dto);

    VehicleDocumentDTO getDocumentById(UUID id);

    List<VehicleDocumentDTO> getAllDocuments();

    List<VehicleDocumentDTO> getDocumentsByVehicleId(UUID vehicleId);

    VehicleDocumentDTO updateDocument(UUID id, VehicleDocumentDTO dto);

    void deleteDocument(UUID id);

    VehicleDocumentDTO uploadDocument(MultipartFile file, String title, VehicleType type, Integer year, UUID vehicleId);

    ResponseEntity<Resource> downloadDocument(UUID id);
}
