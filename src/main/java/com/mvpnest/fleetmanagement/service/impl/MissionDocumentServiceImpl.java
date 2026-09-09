package com.mvpnest.fleetmanagement.service.impl;


import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.UpdateMissionDocumentRequest;
import com.mvpnest.fleetmanagement.dto.missiondocument.UploadMissionDocumentRequest;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.MissionDocument;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.mapper.MissionDocumentMapper;
import com.mvpnest.fleetmanagement.repository.MissionDocumentRepository;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.service.HierarchyService;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
import com.mvpnest.fleetmanagement.service.NotificationService;
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
    private final MissionDocumentMapper mapper;
    private final NotificationService notificationService;
    private final HierarchyService hierarchyService;


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


    @Override
    public List<MissionDocumentDTO> getAllDocuments(User currentUser) {

        if (currentUser.getRole() == RoleType.SUPER_ADMIN) {
            return missionDocumentRepository.findAll().stream().map(mapper::toDTO).toList();
        }

        return missionDocumentRepository.findAll().stream().filter(doc -> {

            User owner = doc.getUploadedBy();

            // Legacy/junk data safety net: if uploadedBy was never set,
            // fall back to the mission's driver as the closest known owner.
            if (owner == null && doc.getMission() != null) {
                owner = doc.getMission().getDriver();
            }

            return hierarchyService.isInHierarchy(currentUser, owner);

        }).map(mapper::toDTO).toList();

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
    public MissionDocumentDTO uploadDocument(MultipartFile file, UploadMissionDocumentRequest request, User currentUser) {

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

            MissionDocument document = MissionDocument.builder().title(request.getTitle()).fileUrl(filename).mission(mission).uploadedBy(currentUser).build();

            MissionDocument saved = missionDocumentRepository.save(document);

            notificationService.notifyMissionDocumentUploaded(saved);

            return mapper.toDTO(saved);

        } catch (IOException e) {

            throw new RuntimeException("Upload failed: " + e.getMessage());

        }

    }


    // =====================================================
    // UPDATE METADATA
    // =====================================================

//    @Override
//    public MissionDocumentDTO updateDocumentTitle(UUID id, String title) {
//
//        MissionDocument document = missionDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Mission document not found"));
//
//        document.setTitle(title);
//
//        return mapper.toDTO(missionDocumentRepository.save(document));
//    }


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

    @Override
    public MissionDocumentDTO updateDocument(UUID id, UpdateMissionDocumentRequest request) {

        MissionDocument document = missionDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        document.setTitle(request.getTitle());

        MissionDocument saved = missionDocumentRepository.save(document);

        return mapper.toDTO(saved);

    }

    @Override
    public void deleteDocument(UUID id) {

        MissionDocument document = missionDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        missionDocumentRepository.delete(document);

    }

    @Override
    public Resource preview(UUID id) {

        try {

            MissionDocument document = missionDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

            Path path = Paths.get(uploadDir, "mission-documents", document.getFileUrl());

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