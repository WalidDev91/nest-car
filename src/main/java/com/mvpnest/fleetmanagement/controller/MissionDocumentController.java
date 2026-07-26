package com.mvpnest.fleetmanagement.controller;


import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.UpdateMissionDocumentRequest;
import com.mvpnest.fleetmanagement.dto.missiondocument.UploadMissionDocumentRequest;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public MissionDocumentDTO upload(@RequestPart("file") MultipartFile file, @ModelAttribute UploadMissionDocumentRequest request) {


        return missionDocumentService.uploadDocument(file, request);

    }


    // =====================================================
    // UPDATE METADATA
    // =====================================================

    @PutMapping("/{id}")
    public MissionDocumentDTO update(@PathVariable UUID id, @RequestBody UpdateMissionDocumentRequest request) {


        return missionDocumentService.updateDocument(id, request);

    }


    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {


        missionDocumentService.deleteDocument(id);

    }


    // =====================================================
    // DOWNLOAD
    // =====================================================

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {


        return missionDocumentService.downloadDocument(id);

    }


}