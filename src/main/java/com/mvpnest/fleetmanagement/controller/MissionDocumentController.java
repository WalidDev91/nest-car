package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.MissionDocumentUploadRequest;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mission-documents")
@RequiredArgsConstructor
public class MissionDocumentController {

    private final MissionDocumentService missionDocumentService;

    @PostMapping
    public MissionDocumentDTO create(@RequestBody MissionDocumentDTO dto) {
        return missionDocumentService.createDocument(dto);
    }

    @GetMapping("/{id}")
    public MissionDocumentDTO getById(@PathVariable UUID id) {
        return missionDocumentService.getDocumentById(id);
    }

    @GetMapping
    public List<MissionDocumentDTO> getAll() {
        return missionDocumentService.getAllDocuments();
    }

    @GetMapping("/mission/{missionId}")
    public List<MissionDocumentDTO> getByMission(@PathVariable UUID missionId) {
        return missionDocumentService.getDocumentsByMissionId(missionId);
    }

    @PutMapping("/{id}")
    public MissionDocumentDTO update(
            @PathVariable UUID id,
            @RequestBody MissionDocumentDTO dto
    ) {
        return missionDocumentService.updateDocument(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        missionDocumentService.deleteDocument(id);
    }

    @PostMapping("/upload")
    public MissionDocumentDTO upload(
            @RequestPart("file") MultipartFile file,
            @ModelAttribute MissionDocumentUploadRequest request
    ) {
        return missionDocumentService.uploadDocument(
                file,
                request.getTitle(),
                request.getMissionId()
        );
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {
        return missionDocumentService.downloadDocument(id);
    }
}
