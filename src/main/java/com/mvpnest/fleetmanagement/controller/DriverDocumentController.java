package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.entity.DriverDocument;
import com.mvpnest.fleetmanagement.service.DriverDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/driver-documents")
@RequiredArgsConstructor
public class DriverDocumentController {

    private final DriverDocumentService documentService;

    @PostMapping
    public DriverDocument create(@RequestBody DriverDocument document) {
        return documentService.createDocument(document);
    }

    @GetMapping("/{id}")
    public DriverDocument getById(@PathVariable UUID id) {
        return documentService.getDocumentById(id);
    }

    @GetMapping
    public List<DriverDocument> getAll() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/driver/{driverId}")
    public List<DriverDocument> getByDriver(@PathVariable UUID driverId) {
        return documentService.getDocumentsByDriverId(driverId);
    }

    @PutMapping("/{id}")
    public DriverDocument update(@PathVariable UUID id, @RequestBody DriverDocument document) {
        return documentService.updateDocument(id, document);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        documentService.deleteDocument(id);
    }
}
