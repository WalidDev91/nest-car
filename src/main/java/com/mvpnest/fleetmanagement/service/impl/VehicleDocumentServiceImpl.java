package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import com.mvpnest.fleetmanagement.enums.VehicleType;
import com.mvpnest.fleetmanagement.mapper.VehicleDocumentMapper;
import com.mvpnest.fleetmanagement.repository.VehicleDocumentRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleDocumentServiceImpl implements VehicleDocumentService {

    private final VehicleRepository vehicleRepository;
    private final VehicleDocumentRepository vehicleDocumentRepository;
    private final VehicleDocumentMapper mapper;
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public VehicleDocumentDTO createDocument(VehicleDocumentDTO dto) {

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        VehicleDocument document = mapper.toEntity(dto);
        document.setVehicle(vehicle);

        return mapper.toDTO(vehicleDocumentRepository.save(document));
    }

    @Override
    public VehicleDocumentDTO getDocumentById(UUID id) {

        VehicleDocument document = vehicleDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VehicleDocument not found with id: " + id));

        return mapper.toDTO(document);
    }

    @Override
    public List<VehicleDocumentDTO> getAllDocuments() {

        return vehicleDocumentRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<VehicleDocumentDTO> getDocumentsByVehicleId(UUID vehicleId) {

        return vehicleDocumentRepository.findByVehicleId(vehicleId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public VehicleDocumentDTO updateDocument(UUID id, VehicleDocumentDTO dto) {

        VehicleDocument existing = vehicleDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VehicleDocument not found with id: " + id));

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        existing.setTitle(dto.getTitle());
        existing.setType(dto.getType());
        existing.setFileUrl(dto.getFileUrl());
        existing.setYear(dto.getYear());
        existing.setVehicle(vehicle);

        return mapper.toDTO(vehicleDocumentRepository.save(existing));
    }

    @Override
    public void deleteDocument(UUID id) {

        VehicleDocument document = vehicleDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VehicleDocument not found with id: " + id));

        vehicleDocumentRepository.delete(document);
    }

    @Override
    public VehicleDocumentDTO uploadDocument(MultipartFile file, String title, VehicleType type, Integer year, UUID vehicleId) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            Path uploadPath = Paths.get(uploadDir, "vehicle-documents");
            Files.createDirectories(uploadPath);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), filePath);

            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));

            VehicleDocument document = VehicleDocument.builder()
                    .title(title)
                    .type(type)
                    .year(year)
                    .fileUrl(filename)
                    .vehicle(vehicle)
                    .build();

            return mapper.toDTO(vehicleDocumentRepository.save(document));

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {

        try {
            VehicleDocument doc = vehicleDocumentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document not found"));

            String filename = doc.getFileUrl();

            Path filePath = Paths.get(uploadDir, "vehicle-documents")
                    .resolve(filename)
                    .normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found or not readable: " + filename);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Download failed: " + e.getMessage());
        }
    }
}
