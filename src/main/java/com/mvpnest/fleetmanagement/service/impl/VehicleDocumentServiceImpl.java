package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import com.mvpnest.fleetmanagement.mapper.VehicleDocumentMapper;
import com.mvpnest.fleetmanagement.repository.VehicleDocumentRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleDocumentServiceImpl implements VehicleDocumentService {

    private final VehicleDocumentRepository repository;
    private final VehicleRepository vehicleRepository;
    private final VehicleDocumentMapper mapper;

    @Override
    public VehicleDocumentDTO createDocument(VehicleDocumentDTO dto) {

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        VehicleDocument document = mapper.toEntity(dto);
        document.setVehicle(vehicle);

        return mapper.toDTO(repository.save(document));
    }

    @Override
    public VehicleDocumentDTO getDocumentById(UUID id) {

        VehicleDocument document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VehicleDocument not found with id: " + id));

        return mapper.toDTO(document);
    }

    @Override
    public List<VehicleDocumentDTO> getAllDocuments() {

        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<VehicleDocumentDTO> getDocumentsByVehicleId(UUID vehicleId) {

        return repository.findByVehicleId(vehicleId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public VehicleDocumentDTO updateDocument(UUID id, VehicleDocumentDTO dto) {

        VehicleDocument existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VehicleDocument not found with id: " + id));

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        existing.setTitle(dto.getTitle());
        existing.setType(dto.getType());
        existing.setFileUrl(dto.getFileUrl());
        existing.setYear(dto.getYear());
        existing.setVehicle(vehicle);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void deleteDocument(UUID id) {

        VehicleDocument document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VehicleDocument not found with id: " + id));

        repository.delete(document);
    }
}
