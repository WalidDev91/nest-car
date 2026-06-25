package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.dto.DriverDocumentUploadRequest;
import com.mvpnest.fleetmanagement.service.DriverDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/driver-documents")
@RequiredArgsConstructor
public class DriverDocumentController {

    private final DriverDocumentService documentService;

    @PostMapping
    public DriverDocumentDTO create(@RequestBody DriverDocumentDTO dto) {
        return documentService.createDocument(dto);
    }

    @GetMapping("/{id}")
    public DriverDocumentDTO getById(@PathVariable UUID id) {
        return documentService.getDocumentById(id);
    }

    @GetMapping
    public List<DriverDocumentDTO> getAll() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/driver/{driverId}")
    public List<DriverDocumentDTO> getByDriver(@PathVariable UUID driverId) {
        return documentService.getDocumentsByDriverId(driverId);
    }

    @PutMapping("/{id}")
    public DriverDocumentDTO update(
            @PathVariable UUID id,
            @RequestBody DriverDocumentDTO dto
    ) {
        return documentService.updateDocument(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        documentService.deleteDocument(id);
    }

    @PostMapping("/upload")
    public DriverDocumentDTO upload(
            @RequestPart("file") MultipartFile file,
            @ModelAttribute DriverDocumentUploadRequest request
    ) {
        return documentService.uploadDocument(
                file,
                request.getTitle(),
                request.getType(),
                request.getDriverId()
        );
    }
}
