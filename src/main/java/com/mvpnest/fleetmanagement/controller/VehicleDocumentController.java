package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.vehicledocument.UploadVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.dto.vehicledocument.UpdateVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicle-documents")
@RequiredArgsConstructor
public class VehicleDocumentController {

    private final VehicleDocumentService vehicleDocumentService;


    // =====================================================
    // GET ONE
    // =====================================================

    @GetMapping("/{id}")
    public VehicleDocumentDTO getById(
            @PathVariable UUID id
    ) {

        return vehicleDocumentService.getDocumentById(id);
    }





    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public List<VehicleDocumentDTO> getAll() {

        return vehicleDocumentService.getAllDocuments();

    }





    // =====================================================
    // GET BY VEHICLE
    // =====================================================

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehicleDocumentDTO> getByVehicle(
            @PathVariable UUID vehicleId
    ) {

        return vehicleDocumentService
                .getDocumentsByVehicleId(vehicleId);

    }





    // =====================================================
    // UPLOAD
    // =====================================================

    @PostMapping("/upload")
    public VehicleDocumentDTO upload(
            @RequestPart("file") MultipartFile file,
            @ModelAttribute UploadVehicleDocumentRequest request
    ) {


        return vehicleDocumentService.uploadDocument(
                file,
                request
        );

    }





    // =====================================================
    // UPDATE METADATA
    // =====================================================

    @PutMapping("/{id}")
    public VehicleDocumentDTO update(
            @PathVariable UUID id,
            @RequestBody UpdateVehicleDocumentRequest request
    ) {


        return vehicleDocumentService.updateDocument(
                id,
                request
        );

    }





    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id
    ) {

        vehicleDocumentService.deleteDocument(id);

    }





    // =====================================================
    // DOWNLOAD
    // =====================================================

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(
            @PathVariable UUID id
    ) {

        return vehicleDocumentService.downloadDocument(id);

    }


}