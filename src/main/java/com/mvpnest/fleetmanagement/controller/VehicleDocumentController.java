package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.dto.VehicleDocumentUploadRequest;
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

    @PostMapping
    public VehicleDocumentDTO create(@RequestBody VehicleDocumentDTO dto) {
        return vehicleDocumentService.createDocument(dto);
    }

    @GetMapping("/{id}")
    public VehicleDocumentDTO getById(@PathVariable UUID id) {
        return vehicleDocumentService.getDocumentById(id);
    }

    @GetMapping
    public List<VehicleDocumentDTO> getAll() {
        return vehicleDocumentService.getAllDocuments();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehicleDocumentDTO> getByVehicle(@PathVariable UUID vehicleId) {
        return vehicleDocumentService.getDocumentsByVehicleId(vehicleId);
    }

    @PutMapping("/{id}")
    public VehicleDocumentDTO update(
            @PathVariable UUID id,
            @RequestBody VehicleDocumentDTO dto
    ) {
        return vehicleDocumentService.updateDocument(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        vehicleDocumentService.deleteDocument(id);
    }

    @PostMapping("/upload")
    public VehicleDocumentDTO upload(
            @RequestPart("file") MultipartFile file,
            @ModelAttribute VehicleDocumentUploadRequest request
    ) {
        return vehicleDocumentService.uploadDocument(
                file,
                request.getTitle(),
                request.getType(),
                request.getYear(),
                request.getVehicleId()
        );
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {
        return vehicleDocumentService.downloadDocument(id);
    }


}
