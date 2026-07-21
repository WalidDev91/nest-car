package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.entity.DriverDocument;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import com.mvpnest.fleetmanagement.mapper.DriverDocumentMapper;
import com.mvpnest.fleetmanagement.repository.DriverDocumentRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.DriverDocumentService;
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
public class DriverDocumentServiceImpl implements DriverDocumentService {

    private final DriverDocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DriverDocumentMapper driverDocumentMapper;
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public DriverDocumentDTO createDocument(DriverDocumentDTO dto) {

        DriverDocument document = driverDocumentMapper.toEntity(dto);

        if (dto.getDriverId() != null) {
            User driver = userRepository.findById(dto.getDriverId())
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
            document.setDriver(driver);
        }

        return driverDocumentMapper.toDTO(documentRepository.save(document));
    }

    @Override
    public DriverDocumentDTO getDocumentById(UUID id) {

        DriverDocument document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DriverDocument not found with id: " + id));

        return driverDocumentMapper.toDTO(document);
    }

    @Override
    public List<DriverDocumentDTO> getAllDocuments() {

        return documentRepository.findAll()
                .stream()
                .map(driverDocumentMapper::toDTO)
                .toList();
    }

    @Override
    public List<DriverDocumentDTO> getMyDocuments(UUID userId) {
        return documentRepository.findByDriverId(userId)
                .stream()
                .map(driverDocumentMapper::toDTO)
                .toList();
    }

    @Override
    public DriverDocumentDTO updateDocument(UUID id, DriverDocumentDTO dto) {

        DriverDocument existing = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DriverDocument not found with id: " + id));

        existing.setTitle(dto.getTitle());
        existing.setType(dto.getType());
        existing.setFileUrl(dto.getFileUrl());
        existing.setStatus(dto.getStatus());
        existing.setUploadedAt(dto.getUploadedAt());
        existing.setValidatedAt(dto.getValidatedAt());

        if (dto.getDriverId() != null) {
            User driver = userRepository.findById(dto.getDriverId())
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
            existing.setDriver(driver);
        }

        return driverDocumentMapper.toDTO(documentRepository.save(existing));
    }

    @Override
    public void deleteDocument(UUID id) {

        DriverDocument document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DriverDocument not found with id: " + id));

        documentRepository.delete(document);
    }

    @Override
    public DriverDocumentDTO uploadDocument(
            MultipartFile file,
            String title,
            DriverDocumentType type,
            UUID driverId
    ) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // create folder
            Path uploadPath = Paths.get(uploadDir, "driver-documents");
            Files.createDirectories(uploadPath);

            // safe filename
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(filename);

            // save file
            Files.copy(file.getInputStream(), filePath);

            User driver = userRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));

            // IMPORTANT: store ONLY filename
            DriverDocument document = DriverDocument.builder()
                    .title(title)
                    .type(type)
                    .status(DriverDocumentStatus.PENDING)
                    .fileUrl(filename)   // ✅ ONLY THIS
                    .uploadedAt(LocalDateTime.now())
                    .driver(driver)
                    .build();

            return driverDocumentMapper.toDTO(documentRepository.save(document));

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {

        try {
            DriverDocument doc = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document not found"));

            String filename = doc.getFileUrl(); // ONLY filename

            Path filePath = Paths.get(uploadDir, "driver-documents")
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

    @Override
    public DriverDocumentDTO updateStatus(UUID id, DriverDocumentStatus status) {

        DriverDocument document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("DriverDocument not found with id: " + id));

        document.setStatus(status);

        return driverDocumentMapper.toDTO(
                documentRepository.save(document)
        );
    }
}
