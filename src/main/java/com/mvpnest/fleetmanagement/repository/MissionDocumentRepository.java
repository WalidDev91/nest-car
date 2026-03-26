package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.MissionDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MissionDocumentRepository extends JpaRepository<MissionDocument, UUID> {

    // Get all documents for a mission
    List<MissionDocument> findByMissionId(UUID missionId);
}