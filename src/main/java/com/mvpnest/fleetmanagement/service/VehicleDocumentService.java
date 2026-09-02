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

    VehicleDocumentDTO uploadDocument(MultipartFile file, UploadVehicleDocumentRequest request, User currentUser);

    VehicleDocumentDTO getDocumentById(UUID id);

    List<VehicleDocumentDTO> getAllDocuments(User currentUser);

    List<VehicleDocumentDTO> getDocumentsByVehicleId(UUID vehicleId);

    ResponseEntity<Resource> downloadDocument(UUID id);

    Resource preview(UUID id);

    VehicleDocumentDTO updateDocument(UUID id, UpdateVehicleDocumentRequest request);

    void deleteDocument(UUID id);

}
