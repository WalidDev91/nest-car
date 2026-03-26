package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.DriverDocument;
import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriverDocumentRepository extends JpaRepository<DriverDocument, UUID> {

    List<DriverDocument> findByDriverId(UUID driverId);

    List<DriverDocument> findByStatus(DriverDocumentStatus status);
}
