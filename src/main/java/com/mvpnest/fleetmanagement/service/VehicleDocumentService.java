package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.vehicledocument.UpdateVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.UploadVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface VehicleDocumentService {

    VehicleDocumentDTO uploadDocument(MultipartFile file, UploadVehicleDocumentRequest request);

    VehicleDocumentDTO updateDocument(UUID id, UpdateVehicleDocumentRequest request);

    VehicleDocumentDTO getDocumentById(UUID id);

    List<VehicleDocumentDTO> getAllDocuments(User currentUser);

    List<VehicleDocumentDTO> getDocumentsByVehicleId(UUID vehicleId);

    void deleteDocument(UUID id);

    ResponseEntity<Resource> downloadDocument(UUID id);

}
