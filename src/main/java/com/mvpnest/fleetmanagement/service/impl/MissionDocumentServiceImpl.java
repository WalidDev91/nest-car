package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.entity.Mission;
import  com.mvpnest.fleetmanagement.entity.MissionDocument;
import com.mvpnest.fleetmanagement.mapper.MissionDocumentMapper;
import com.mvpnest.fleetmanagement.repository.MissionDocumentRepository;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
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
public class MissionDocumentServiceImpl implements MissionDocumentService {

    private final MissionRepository missionRepository;
    private final MissionDocumentRepository missionDocumentRepository;
    private final MissionDocumentMapper mapper;
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public MissionDocumentDTO createDocument(MissionDocumentDTO dto) {

        Mission mission = missionRepository.findById(dto.getMissionId())
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        MissionDocument document = MissionDocument.builder()
                .title(dto.getTitle())
                .fileUrl(dto.getFileUrl())
                .mission(mission)
                .build();

        return mapper.toDTO(missionDocumentRepository.save(document));
    }

    @Override
    public MissionDocumentDTO getDocumentById(UUID id) {
        return mapper.toDTO(
                missionDocumentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("MissionDocument not found with id: " + id))
        );
    }

    @Override
    public List<MissionDocumentDTO> getAllDocuments() {
        return missionDocumentRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<MissionDocumentDTO> getDocumentsByMissionId(UUID missionId) {
        return missionDocumentRepository.findByMissionId(missionId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public MissionDocumentDTO updateDocument(UUID id, MissionDocumentDTO dto) {

        MissionDocument existing = missionDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MissionDocument not found with id: " + id));

        Mission mission = missionRepository.findById(dto.getMissionId())
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        existing.setTitle(dto.getTitle());
        existing.setFileUrl(dto.getFileUrl());
        existing.setMission(mission);

        return mapper.toDTO(missionDocumentRepository.save(existing));
    }

    @Override
    public void deleteDocument(UUID id) {
        missionDocumentRepository.deleteById(id);
    }

    @Override
    public MissionDocumentDTO uploadDocument(
            MultipartFile file,
            String title,
            UUID missionId
    ) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            Path uploadPath = Paths.get(uploadDir, "mission-documents");
            Files.createDirectories(uploadPath);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), filePath);

            Mission mission = missionRepository.findById(missionId)
                    .orElseThrow(() -> new RuntimeException("Mission not found"));

            MissionDocument document = MissionDocument.builder()
                    .title(title)
                    .fileUrl(filename)
                    .mission(mission)
                    .build();

            return mapper.toDTO(missionDocumentRepository.save(document));

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {

        try {
            MissionDocument doc = missionDocumentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document not found"));

            String filename = doc.getFileUrl();

            Path filePath = Paths.get(uploadDir, "mission-documents")
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

