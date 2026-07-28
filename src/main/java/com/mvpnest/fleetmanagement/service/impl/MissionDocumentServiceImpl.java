package com.mvpnest.fleetmanagement.service.impl;


import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.UpdateMissionDocumentRequest;
import com.mvpnest.fleetmanagement.dto.missiondocument.UploadMissionDocumentRequest;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.MissionDocument;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.mapper.MissionDocumentMapper;
import com.mvpnest.fleetmanagement.repository.MissionDocumentRepository;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
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
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MissionDocumentServiceImpl implements MissionDocumentService {


    private final MissionRepository missionRepository;
    private final MissionDocumentRepository missionDocumentRepository;
    private final UserRepository userRepository;
    private final MissionDocumentMapper mapper;


    @Value("${app.upload.dir}")
    private String uploadDir;


    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public MissionDocumentDTO getDocumentById(UUID id) {


        MissionDocument document = missionDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Mission document not found"));


        return mapper.toDTO(document);

    }


    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<MissionDocumentDTO> getAllDocuments(User currentUser) {

        List<MissionDocument> documents;

        switch (currentUser.getRole()) {

            case SUPER_ADMIN -> documents = missionDocumentRepository.findAll();

            case ADMIN -> {

                List<User> fleetManagers = userRepository.findByAdmin(currentUser);

                List<UUID> fleetManagerIds = fleetManagers.stream().map(User::getId).toList();

                documents = missionDocumentRepository.findAll().stream().filter(doc -> doc.getMission() != null && doc.getMission().getDriver() != null && doc.getMission().getDriver().getAdmin() != null && fleetManagerIds.contains(doc.getMission().getDriver().getAdmin().getId())).toList();
            }

            case FLEET_MANAGER -> {

                List<User> drivers = userRepository.findByAdmin(currentUser);

                List<UUID> driverIds = drivers.stream().map(User::getId).toList();

                documents = missionDocumentRepository.findAll().stream().filter(doc -> doc.getMission() != null && doc.getMission().getDriver() != null && driverIds.contains(doc.getMission().getDriver().getId())).toList();

            }

            case DRIVER -> {

                User driver = userRepository.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("Driver not found"));

                List<UUID> missionIds = driver.getMissions().stream().map(Mission::getId).toList();
                documents = missionDocumentRepository.findAll().stream().filter(doc -> missionIds.contains(doc.getMission().getId())).toList();

            }

            default -> documents = List.of();

        }

        return documents.stream().map(mapper::toDTO).toList();

    }


    // =====================================================
    // GET BY MISSION
    // =====================================================

    @Override
    public List<MissionDocumentDTO> getDocumentsByMissionId(UUID missionId) {


        return missionDocumentRepository.findByMissionId(missionId).stream().map(mapper::toDTO).toList();

    }


    // =====================================================
    // UPLOAD DOCUMENT
    // =====================================================

    @Override
    public MissionDocumentDTO uploadDocument(MultipartFile file, UploadMissionDocumentRequest request) {


        try {


            if (file.isEmpty()) {

                throw new RuntimeException("File is empty");

            }


            Mission mission = missionRepository.findById(request.getMissionId()).orElseThrow(() -> new RuntimeException("Mission not found"));


            Path uploadPath = Paths.get(uploadDir, "mission-documents");


            Files.createDirectories(uploadPath);


            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();


            Path filePath = uploadPath.resolve(filename);


            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


            MissionDocument document = MissionDocument.builder().title(request.getTitle()).fileUrl(filename).mission(mission).build();


            return mapper.toDTO(missionDocumentRepository.save(document));


        } catch (IOException e) {


            throw new RuntimeException("Upload failed: " + e.getMessage());

        }

    }


    // =====================================================
    // UPDATE METADATA
    // =====================================================

    @Override
    public MissionDocumentDTO updateDocument(UUID id, UpdateMissionDocumentRequest request) {


        MissionDocument document = missionDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Mission document not found"));


        document.setTitle(request.getTitle());


        return mapper.toDTO(missionDocumentRepository.save(document));

    }


    // =====================================================
    // DELETE
    // =====================================================

    @Override
    public void deleteDocument(UUID id) {


        MissionDocument document = missionDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Mission document not found"));


        missionDocumentRepository.delete(document);

    }


    // =====================================================
    // DOWNLOAD
    // =====================================================

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {

        try {

            MissionDocument document = missionDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Mission document not found"));

            Path path = Paths.get(uploadDir, "mission-documents", document.getFileUrl());

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

}