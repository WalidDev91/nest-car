package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.vehicledocument.UpdateVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.UploadVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import com.mvpnest.fleetmanagement.mapper.VehicleDocumentMapper;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.repository.VehicleDocumentRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleDocumentServiceImpl implements VehicleDocumentService {


    private final VehicleRepository vehicleRepository;
    private final VehicleDocumentRepository vehicleDocumentRepository;
    private final UserRepository userRepository;
    private final VehicleDocumentMapper mapper;
    private final MissionRepository missionRepository;


    @Value("${app.upload.dir}")
    private String uploadDir;


    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public VehicleDocumentDTO getDocumentById(UUID id) {

        VehicleDocument document = vehicleDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle document not found"));

        return mapper.toDTO(document);
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @Transactional(readOnly = true)
    @Override
    public List<VehicleDocumentDTO> getAllDocuments(User currentUser) {

        List<VehicleDocument> documents;

        switch (currentUser.getRole()) {

            case SUPER_ADMIN -> documents = vehicleDocumentRepository.findAll();

            case ADMIN -> documents = vehicleDocumentRepository.findAll().stream().filter(doc -> {

                if (doc.getVehicle() == null || doc.getVehicle().getAdmin() == null) return false;

                User creator = doc.getVehicle().getAdmin();

                // Admin sees their own vehicles, plus any created by a
                // Fleet Manager who reports to them.
                return creator.getId().equals(currentUser.getId()) || (creator.getAdmin() != null && creator.getAdmin().getId().equals(currentUser.getId()));

            }).toList();

            case FLEET_MANAGER ->
                    documents = vehicleDocumentRepository.findAll().stream().filter(doc -> doc.getVehicle() != null && doc.getVehicle().getAdmin() != null && doc.getVehicle().getAdmin().getId().equals(currentUser.getId())).toList();

            case DRIVER -> {

                List<UUID> vehicleIds = missionRepository.findByDriverId(currentUser.getId()).stream().filter(m -> m.getVehicle() != null).map(m -> m.getVehicle().getId()).distinct().toList();

                documents = vehicleDocumentRepository.findAll().stream().filter(doc -> doc.getVehicle() != null && vehicleIds.contains(doc.getVehicle().getId())).toList();
            }

            default -> documents = List.of();

        }

        return documents.stream().map(mapper::toDTO).toList();

    }


    // =====================================================
    // GET BY VEHICLE
    // =====================================================

    @Override
    public List<VehicleDocumentDTO> getDocumentsByVehicleId(UUID vehicleId) {

        return vehicleDocumentRepository.findByVehicleId(vehicleId).stream().map(mapper::toDTO).toList();
    }


    // =====================================================
    // UPLOAD DOCUMENT
    // =====================================================

    @Override
    public VehicleDocumentDTO uploadDocument(MultipartFile file, UploadVehicleDocumentRequest request) {

        try {

            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(() -> new RuntimeException("Vehicle not found"));

            Path uploadPath = Paths.get(uploadDir, "vehicle-documents");

            Files.createDirectories(uploadPath);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            VehicleDocument document = VehicleDocument.builder().title(request.getTitle()).type(request.getType()).year(request.getYear()).fileUrl(filename).vehicle(vehicle).build();

            return mapper.toDTO(vehicleDocumentRepository.save(document));

        } catch (IOException e) {

            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }


    // =====================================================
    // DOWNLOAD
    // =====================================================

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {

        try {

            VehicleDocument document = vehicleDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

            Path path = Paths.get(uploadDir, "vehicle-documents", document.getFileUrl());

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            // Detect the real file type (PNG, JPG, PDF, ...)
            String contentType = Files.probeContentType(path);

            if (contentType == null || contentType.isBlank()) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            // Remove UUID prefix from downloaded filename
            String storedName = document.getFileUrl();
            String downloadName = storedName.substring(storedName.indexOf('_') + 1);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadName + "\"").contentType(MediaType.parseMediaType(contentType)).body(resource);

        } catch (Exception e) {

            throw new RuntimeException("Download failed: " + e.getMessage(), e);

        }

    }

    @Override
    public VehicleDocumentDTO updateDocument(UUID id, UpdateVehicleDocumentRequest request) {

        VehicleDocument document = vehicleDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        document.setTitle(request.getTitle());
        document.setType(request.getType());
        document.setYear(request.getYear());

        VehicleDocument saved = vehicleDocumentRepository.save(document);

        return mapper.toDTO(saved);

    }

    @Override
    public void deleteDocument(UUID id) {

        VehicleDocument document = vehicleDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        vehicleDocumentRepository.delete(document);

    }

    @Override
    public Resource preview(UUID id) {

        try {

            VehicleDocument document = vehicleDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

            Path path = Paths.get(uploadDir, "vehicle-documents", document.getFileUrl());

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            return resource;

        } catch (Exception e) {

            throw new RuntimeException("Preview failed");

        }

    }
}