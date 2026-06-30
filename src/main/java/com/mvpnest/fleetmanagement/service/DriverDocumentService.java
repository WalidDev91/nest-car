package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DriverDocumentService {

    DriverDocumentDTO createDocument(DriverDocumentDTO dto);

    DriverDocumentDTO getDocumentById(UUID id);

    List<DriverDocumentDTO> getAllDocuments();

    List<DriverDocumentDTO> getDocumentsByDriverId(UUID driverId);

    DriverDocumentDTO updateDocument(UUID id, DriverDocumentDTO dto);

    void deleteDocument(UUID id);

    DriverDocumentDTO uploadDocument(MultipartFile file, String title, DriverDocumentType type, UUID driverId);

    ResponseEntity<Resource> downloadDocument(UUID id);

}
