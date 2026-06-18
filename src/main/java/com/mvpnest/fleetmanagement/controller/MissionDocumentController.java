package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mission-documents")
@RequiredArgsConstructor
public class MissionDocumentController {

    private final MissionDocumentService service;

    @PostMapping
    public MissionDocumentDTO create(@RequestBody MissionDocumentDTO dto) {
        return service.createDocument(dto);
    }

    @GetMapping("/{id}")
    public MissionDocumentDTO getById(@PathVariable UUID id) {
        return service.getDocumentById(id);
    }

    @GetMapping
    public List<MissionDocumentDTO> getAll() {
        return service.getAllDocuments();
    }

    @GetMapping("/mission/{missionId}")
    public List<MissionDocumentDTO> getByMission(@PathVariable UUID missionId) {
        return service.getDocumentsByMissionId(missionId);
    }

    @PutMapping("/{id}")
    public MissionDocumentDTO update(
            @PathVariable UUID id,
            @RequestBody MissionDocumentDTO dto
    ) {
        return service.updateDocument(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteDocument(id);
    }
}
