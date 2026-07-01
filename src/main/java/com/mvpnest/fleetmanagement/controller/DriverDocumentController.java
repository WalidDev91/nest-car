package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.dto.DriverDocumentUploadRequest;
import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import com.mvpnest.fleetmanagement.service.DriverDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/driver-documents")
@RequiredArgsConstructor
public class DriverDocumentController {

    private final DriverDocumentService driverDocumentService;

    @PostMapping
    public DriverDocumentDTO create(@RequestBody DriverDocumentDTO dto) {
        return driverDocumentService.createDocument(dto);
    }

    @GetMapping("/{id}")
    public DriverDocumentDTO getById(@PathVariable UUID id) {
        return driverDocumentService.getDocumentById(id);
    }

    @GetMapping
    public List<DriverDocumentDTO> getAll() {
        return driverDocumentService.getAllDocuments();
    }

    @GetMapping("/driver/{driverId}")
    public List<DriverDocumentDTO> getByDriver(@PathVariable UUID driverId) {
        return driverDocumentService.getDocumentsByDriverId(driverId);
    }

    @PutMapping("/{id}")
    public DriverDocumentDTO update(
            @PathVariable UUID id,
            @RequestBody DriverDocumentDTO dto
    ) {
        return driverDocumentService.updateDocument(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        driverDocumentService.deleteDocument(id);
    }

    @PostMapping("/upload")
    public DriverDocumentDTO upload(
            @RequestPart("file") MultipartFile file,
            @ModelAttribute DriverDocumentUploadRequest request
    ) {
        return driverDocumentService.uploadDocument(
                file,
                request.getTitle(),
                request.getType(),
                request.getDriverId()
        );
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {
        return driverDocumentService.downloadDocument(id);
    }

    @PatchMapping("/{id}/status")
    public DriverDocumentDTO updateStatus(
            @PathVariable UUID id,
            @RequestParam DriverDocumentStatus status
    ) {
        return driverDocumentService.updateStatus(id, status);
    }
}
