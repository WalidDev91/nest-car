package com.mvpnest.fleetmanagement.controller;


import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.UpdateMissionDocumentRequest;
import com.mvpnest.fleetmanagement.dto.missiondocument.UploadMissionDocumentRequest;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/mission-documents")
@RequiredArgsConstructor
public class MissionDocumentController {


    private final MissionDocumentService missionDocumentService;


    // =====================================================
    // GET ONE
    // =====================================================

    @GetMapping("/{id}")
    public MissionDocumentDTO getById(@PathVariable UUID id) {

        return missionDocumentService.getDocumentById(id);

    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public List<MissionDocumentDTO> getAll(@AuthenticationPrincipal User user) {

        return missionDocumentService.getAllDocuments(user);

    }


    // =====================================================
    // GET BY MISSION
    // =====================================================

    @GetMapping("/mission/{missionId}")
    public List<MissionDocumentDTO> getByMission(@PathVariable UUID missionId) {

        return missionDocumentService.getDocumentsByMissionId(missionId);

    }


    // =====================================================
    // UPLOAD DOCUMENT
    // =====================================================

    @PostMapping("/upload")
    public MissionDocumentDTO upload(@RequestPart("file") MultipartFile file, @ModelAttribute UploadMissionDocumentRequest request, @AuthenticationPrincipal User user) {
        return missionDocumentService.uploadDocument(file, request, user);
    }


    // =====================================================
    // UPDATE METADATA
    // =====================================================

//    @PatchMapping("/{id}")
//    public MissionDocumentDTO updateTitle(@PathVariable UUID id, @RequestParam String title) {
//        return missionDocumentService.updateDocumentTitle(id, title);
//    }


    // =====================================================
    // DOWNLOAD
    // =====================================================

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {


        return missionDocumentService.downloadDocument(id);

    }

    @PutMapping("/{id}")
    public ResponseEntity<MissionDocumentDTO> updateDocument(@PathVariable UUID id, @RequestBody UpdateMissionDocumentRequest request) {

        return ResponseEntity.ok(missionDocumentService.updateDocument(id, request));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {

        missionDocumentService.deleteDocument(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<Resource> preview(@PathVariable UUID id) {

        try {

            Resource resource = missionDocumentService.preview(id);

            String contentType = Files.probeContentType(resource.getFile().toPath());

            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

        } catch (Exception e) {

            throw new RuntimeException("Preview failed");

        }

    }

}