package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import com.mvpnest.fleetmanagement.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicle-documents")
@RequiredArgsConstructor
public class VehicleDocumentController {

    private final VehicleDocumentService service;

    @PostMapping
    public VehicleDocument create(@RequestBody VehicleDocument document) {
        return service.createDocument(document);
    }

    @GetMapping("/{id}")
    public VehicleDocument getById(@PathVariable UUID id) {
        return service.getDocumentById(id);
    }

    @GetMapping
    public List<VehicleDocument> getAll() {
        return service.getAllDocuments();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehicleDocument> getByVehicle(@PathVariable UUID vehicleId) {
        return service.getDocumentsByVehicleId(vehicleId);
    }

    @PutMapping("/{id}")
    public VehicleDocument update(@PathVariable UUID id, @RequestBody VehicleDocument document) {
        return service.updateDocument(id, document);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteDocument(id);
    }
}
