package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.entity.MissionDocument;
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
    public MissionDocument create(@RequestBody MissionDocument document) {
        return service.createDocument(document);
    }

    @GetMapping("/{id}")
    public MissionDocument getById(@PathVariable UUID id) {
        return service.getDocumentById(id);
    }

    @GetMapping
    public List<MissionDocument> getAll() {
        return service.getAllDocuments();
    }

    @GetMapping("/mission/{missionId}")
    public List<MissionDocument> getByMission(@PathVariable UUID missionId) {
        return service.getDocumentsByMissionId(missionId);
    }

    @PutMapping("/{id}")
    public MissionDocument update(@PathVariable UUID id, @RequestBody MissionDocument document) {
        return service.updateDocument(id, document);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteDocument(id);
    }
}
