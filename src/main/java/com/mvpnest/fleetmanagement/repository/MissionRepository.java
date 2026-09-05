package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MissionRepository extends JpaRepository<Mission, UUID> {

    List<Mission> findByDriverId(UUID driverId);

    List<Mission> findByStatus(MissionStatus status);

    List<Mission> findByVehicleId(UUID vehicleId);

    List<Mission> findByDepartureLocation(String departureLocation);

    List<Mission> findByDestinationLocation(String destinationLocation);

    boolean existsByDriverIdAndStartDateLessThanAndEndDateGreaterThan(UUID driverId, LocalDateTime endDate, LocalDateTime startDate);

    boolean existsByVehicleIdAndStartDateLessThanAndEndDateGreaterThan(UUID vehicleId, LocalDateTime endDate, LocalDateTime startDate);

    boolean existsByDriverIdAndStartDateLessThanAndEndDateGreaterThanAndIdNot(UUID driverId, LocalDateTime endDate, LocalDateTime startDate, UUID missionId);

    boolean existsByVehicleIdAndStartDateLessThanAndEndDateGreaterThanAndIdNot(UUID vehicleId, LocalDateTime endDate, LocalDateTime startDate, UUID missionId);
}