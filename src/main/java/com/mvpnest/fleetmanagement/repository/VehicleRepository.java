package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Vehicle findByPlateNumber(String plateNumber);
}
