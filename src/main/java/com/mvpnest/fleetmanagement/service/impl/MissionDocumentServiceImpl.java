package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.entity.Mission;
import  com.mvpnest.fleetmanagement.entity.MissionDocument;
import com.mvpnest.fleetmanagement.mapper.MissionDocumentMapper;
import com.mvpnest.fleetmanagement.repository.MissionDocumentRepository;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionDocumentServiceImpl implements MissionDocumentService {

    private final MissionDocumentRepository repository;
    private final MissionRepository missionRepository;
    private final MissionDocumentMapper mapper;

    @Override
    public MissionDocumentDTO createDocument(MissionDocumentDTO dto) {

        Mission mission = missionRepository.findById(dto.getMissionId())
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        MissionDocument document = MissionDocument.builder()
                .title(dto.getTitle())
                .fileUrl(dto.getFileUrl())
                .mission(mission)
                .build();

        return mapper.toDTO(repository.save(document));
    }

    @Override
    public MissionDocumentDTO getDocumentById(UUID id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("MissionDocument not found with id: " + id))
        );
    }

    @Override
    public List<MissionDocumentDTO> getAllDocuments() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<MissionDocumentDTO> getDocumentsByMissionId(UUID missionId) {
        return repository.findByMissionId(missionId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public MissionDocumentDTO updateDocument(UUID id, MissionDocumentDTO dto) {

        MissionDocument existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MissionDocument not found with id: " + id));

        Mission mission = missionRepository.findById(dto.getMissionId())
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        existing.setTitle(dto.getTitle());
        existing.setFileUrl(dto.getFileUrl());
        existing.setMission(mission);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void deleteDocument(UUID id) {
        repository.deleteById(id);
    }
}
