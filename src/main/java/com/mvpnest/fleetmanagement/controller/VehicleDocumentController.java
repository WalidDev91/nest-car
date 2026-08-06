package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.vehicledocument.UpdateVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.UploadVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
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
    public VehicleDocumentDTO getById(@PathVariable UUID id) {

        return vehicleDocumentService.getDocumentById(id);
    }


    // =====================================================
    // GET ALL
    // =====================================================


    @GetMapping
    public List<VehicleDocumentDTO> getAll(@AuthenticationPrincipal User user) {

        return vehicleDocumentService.getAllDocuments(user);

    }


    // =====================================================
    // GET BY VEHICLE
    // =====================================================

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehicleDocumentDTO> getByVehicle(@PathVariable UUID vehicleId) {

        return vehicleDocumentService.getDocumentsByVehicleId(vehicleId);

    }


    // =====================================================
    // UPLOAD
    // =====================================================

    @PostMapping("/upload")
    public VehicleDocumentDTO upload(@RequestPart("file") MultipartFile file, @ModelAttribute UploadVehicleDocumentRequest request) {


        return vehicleDocumentService.uploadDocument(file, request);

    }


    // =====================================================
    // DOWNLOAD
    // =====================================================

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {

        return vehicleDocumentService.downloadDocument(id);

    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDocumentDTO> updateDocument(@PathVariable UUID id, @RequestBody UpdateVehicleDocumentRequest request) {

        return ResponseEntity.ok(vehicleDocumentService.updateDocument(id, request));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        vehicleDocumentService.deleteDocument(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<Resource> preview(@PathVariable UUID id) {

        try {

            Resource resource = vehicleDocumentService.preview(id);

            String contentType = Files.probeContentType(resource.getFile().toPath());

            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

        } catch (Exception e) {

            throw new RuntimeException("Preview failed");

        }

    }


}