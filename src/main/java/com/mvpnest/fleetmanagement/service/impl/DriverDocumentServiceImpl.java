package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.DriverDocumentDTO;
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
    private final DriverDocumentMapper mapper;
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public DriverDocumentDTO createDocument(DriverDocumentDTO dto) {

        DriverDocument document = mapper.toEntity(dto);

        if (dto.getDriverId() != null) {
            User driver = userRepository.findById(dto.getDriverId())
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
            document.setDriver(driver);
        }

        return mapper.toDTO(documentRepository.save(document));
    }

    @Override
    public DriverDocumentDTO getDocumentById(UUID id) {

        DriverDocument document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DriverDocument not found with id: " + id));

        return mapper.toDTO(document);
    }

    @Override
    public List<DriverDocumentDTO> getAllDocuments() {

        return documentRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<DriverDocumentDTO> getDocumentsByDriverId(UUID driverId) {

        return documentRepository.findByDriverId(driverId)
                .stream()
                .map(mapper::toDTO)
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

        return mapper.toDTO(documentRepository.save(existing));
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
            // 1. validate file
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // 2. create directory safely
            Path uploadPath = Paths.get(uploadDir, "driver-documents");
            Files.createDirectories(uploadPath);

            // 3. unique filename
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), filePath);

            // 4. find driver
            User driver = userRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));

            // 5. build entity
            DriverDocument document = DriverDocument.builder()
                    .title(title)
                    .type(type)
                    .status(DriverDocumentStatus.PENDING)
                    .fileUrl(baseUrl + "/uploads/driver-documents/" + filename)
                    .uploadedAt(LocalDateTime.now())
                    .driver(driver)
                    .build();

            // 6. save
            return mapper.toDTO(documentRepository.save(document));

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }
}
