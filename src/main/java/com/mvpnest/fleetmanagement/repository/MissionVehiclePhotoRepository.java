package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.MissionVehiclePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MissionVehiclePhotoRepository extends JpaRepository<MissionVehiclePhoto, UUID> {
}
