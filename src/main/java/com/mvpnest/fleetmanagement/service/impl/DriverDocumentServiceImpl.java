package com.mvpnest.fleetmanagement.service.impl;


import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.dto.driverdocument.UpdateDriverDocumentRequest;
import com.mvpnest.fleetmanagement.entity.DriverDocument;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import com.mvpnest.fleetmanagement.mapper.DriverDocumentMapper;
import com.mvpnest.fleetmanagement.repository.DriverDocumentRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.DriverDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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


    private final DriverDocumentRepository driverDocumentRepository;
    private final UserRepository userRepository;
    private final DriverDocumentMapper mapper;


    @Value("${app.upload.dir}")
    private String uploadDir;


    @Override
    public DriverDocumentDTO getDocumentById(UUID id) {


        DriverDocument document = driverDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));


        return mapper.toDTO(document);

    }

    @Override
    public List<DriverDocumentDTO> getAllDocuments(User currentUser) {

        List<DriverDocument> documents;

        switch (currentUser.getRole()) {

            case SUPER_ADMIN -> documents = driverDocumentRepository.findAll();

            case ADMIN -> {

                List<User> fleetManagers = userRepository.findByAdmin(currentUser);

                List<UUID> driverIds = fleetManagers.stream().flatMap(fm -> userRepository.findByAdmin(fm).stream()).map(User::getId).toList();

                documents = driverDocumentRepository.findAll().stream().filter(doc -> driverIds.contains(doc.getDriver().getId())).toList();

            }

            case FLEET_MANAGER -> {

                List<User> drivers = userRepository.findByAdmin(currentUser);

                List<UUID> driverIds = drivers.stream().map(User::getId).toList();

                documents = driverDocumentRepository.findAll().stream().filter(doc -> driverIds.contains(doc.getDriver().getId())).toList();

            }

            case DRIVER -> documents = driverDocumentRepository.findByDriverId(currentUser.getId());

            default -> documents = List.of();

        }

        return documents.stream().map(mapper::toDTO).toList();

    }


    @Override
    public List<DriverDocumentDTO> getDocumentsByDriverId(UUID driverId) {

        return driverDocumentRepository.findByDriverId(driverId).stream().map(mapper::toDTO).toList();

    }


    @Override
    public List<DriverDocumentDTO> getDocumentsByStatus(DriverDocumentStatus status) {


        return driverDocumentRepository.findByStatus(status).stream().map(mapper::toDTO).toList();

    }


    @Override
    public DriverDocumentDTO uploadDocument(MultipartFile file, String title, DriverDocumentType type, UUID driverId) {


        try {


            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }


            Path folder = Paths.get(uploadDir, "driver-documents");


            Files.createDirectories(folder);


            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();


            Path path = folder.resolve(filename);


            Files.copy(file.getInputStream(), path);


            User driver = userRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver not found"));


            DriverDocument document = DriverDocument.builder().title(title).type(type).fileUrl(filename).status(DriverDocumentStatus.PENDING).uploadedAt(LocalDateTime.now()).driver(driver).build();


            return mapper.toDTO(driverDocumentRepository.save(document));


        } catch (IOException e) {

            throw new RuntimeException("Upload failed");

        }


    }


    @Override
    public DriverDocumentDTO updateDocument(UUID id, UpdateDriverDocumentRequest request) {

        DriverDocument document = driverDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));


        if (request.getTitle() != null) {
            document.setTitle(request.getTitle());
        }


        if (request.getType() != null) {
            document.setType(request.getType());
        }


        return mapper.toDTO(driverDocumentRepository.save(document));

    }


    @Override
    public void deleteDocument(UUID id) {


        DriverDocument document = driverDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));


        driverDocumentRepository.delete(document);

    }


    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {

        try {

            DriverDocument document = driverDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

            Path path = Paths.get(uploadDir, "driver-documents", document.getFileUrl());

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            // Derive content type from the file itself (extension/signature) —
            // no need to store it separately.
            String contentType = Files.probeContentType(path);
            if (contentType == null || contentType.isBlank()) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            // Strip the "UUID_" prefix so the user downloads a clean, correctly-named file
            String storedName = document.getFileUrl();
            String downloadName = storedName.substring(storedName.indexOf('_') + 1);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadName + "\"").contentType(MediaType.parseMediaType(contentType)).body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Download failed");
        }
    }


    @Override
    public DriverDocumentDTO updateStatus(UUID id, DriverDocumentStatus status) {


        DriverDocument document = driverDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));


        document.setStatus(status);


        if (status == DriverDocumentStatus.APPROVED) {

            document.setValidatedAt(LocalDateTime.now());

        }


        return mapper.toDTO(driverDocumentRepository.save(document));

    }


}