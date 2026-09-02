package com.mvpnest.fleetmanagement.service;


import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.dto.driverdocument.UpdateDriverDocumentRequest;
import com.mvpnest.fleetmanagement.dto.driverdocument.UpdateDriverDocumentStatusRequest;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public interface DriverDocumentService {

    DriverDocumentDTO getDocumentById(UUID id);

    List<DriverDocumentDTO> getAllDocuments(User currentUser);

    List<DriverDocumentDTO> getDocumentsByDriverId(UUID driverId);

    List<DriverDocumentDTO> getDocumentsByStatus(DriverDocumentStatus status);

    DriverDocumentDTO updateDocument(UUID id, UpdateDriverDocumentRequest request);

    void deleteDocument(UUID id);

    DriverDocumentDTO uploadDocument(MultipartFile file, String title, DriverDocumentType type, LocalDate expiryDate, UUID driverId);

    DriverDocumentDTO updateStatus(UUID id, UpdateDriverDocumentStatusRequest request);

    Resource preview(UUID id);

    ResponseEntity<Resource> downloadDocument(UUID id);

}