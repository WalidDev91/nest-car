package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.VehicleDocumentDTO;
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
    public VehicleDocumentDTO create(@RequestBody VehicleDocumentDTO dto) {
        return service.createDocument(dto);
    }

    @GetMapping("/{id}")
    public VehicleDocumentDTO getById(@PathVariable UUID id) {
        return service.getDocumentById(id);
    }

    @GetMapping
    public List<VehicleDocumentDTO> getAll() {
        return service.getAllDocuments();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<VehicleDocumentDTO> getByVehicle(@PathVariable UUID vehicleId) {
        return service.getDocumentsByVehicleId(vehicleId);
    }

    @PutMapping("/{id}")
    public VehicleDocumentDTO update(
            @PathVariable UUID id,
            @RequestBody VehicleDocumentDTO dto
    ) {
        return service.updateDocument(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteDocument(id);
    }
}
