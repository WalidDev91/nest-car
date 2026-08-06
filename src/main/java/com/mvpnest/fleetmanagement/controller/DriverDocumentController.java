package com.mvpnest.fleetmanagement.controller;


import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.dto.driverdocument.UpdateDriverDocumentRequest;
import com.mvpnest.fleetmanagement.dto.driverdocument.UpdateDriverDocumentStatusRequest;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import com.mvpnest.fleetmanagement.service.DriverDocumentService;
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
@RequestMapping("/api/driver-documents")
@RequiredArgsConstructor
public class DriverDocumentController {


    private final DriverDocumentService driverDocumentService;


    // =====================================================
    // UPLOAD DOCUMENT
    // =====================================================

    @PostMapping("/upload")
    public DriverDocumentDTO upload(@RequestPart("file") MultipartFile file, @RequestParam String title, @RequestParam DriverDocumentType type, @RequestParam UUID driverId) {


        return driverDocumentService.uploadDocument(file, title, type, driverId);

    }


    @GetMapping
    public List<DriverDocumentDTO> getAll(@AuthenticationPrincipal User user) {

        return driverDocumentService.getAllDocuments(user);


    }


    // =====================================================
    // GET DOCUMENT BY ID
    // =====================================================

    @GetMapping("/{id}")
    public DriverDocumentDTO getById(@PathVariable UUID id) {


        return driverDocumentService.getDocumentById(id);

    }


    // =====================================================
    // GET DRIVER DOCUMENTS
    // =====================================================

    @GetMapping("/driver/{driverId}")
    public List<DriverDocumentDTO> getByDriver(@PathVariable UUID driverId) {

        return driverDocumentService.getDocumentsByDriverId(driverId);

    }


    // =====================================================
    // FILTER BY STATUS
    // =====================================================

    @GetMapping("/status/{status}")
    public List<DriverDocumentDTO> getByStatus(@PathVariable DriverDocumentStatus status) {


        return driverDocumentService.getDocumentsByStatus(status);

    }


    // =====================================================
    // UPDATE METADATA
    // ONLY TITLE + TYPE
    // =====================================================

    @PutMapping("/{id}")
    public DriverDocumentDTO update(@PathVariable UUID id, @RequestBody UpdateDriverDocumentRequest request) {


        return driverDocumentService.updateDocument(id, request);

    }


    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {


        driverDocumentService.deleteDocument(id);

    }


    // =====================================================
    // DOWNLOAD FILE
    // =====================================================

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {


        return driverDocumentService.downloadDocument(id);

    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<DriverDocumentDTO> updateStatus(@PathVariable UUID id, @RequestBody UpdateDriverDocumentStatusRequest request) {

        return ResponseEntity.ok(driverDocumentService.updateStatus(id, request));

    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<Resource> preview(@PathVariable UUID id) {

        try {

            Resource resource = driverDocumentService.preview(id);

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