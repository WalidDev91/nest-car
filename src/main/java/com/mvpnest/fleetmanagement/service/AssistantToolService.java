package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.dto.mission.MissionDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.dto.user.UserDTO;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;

import java.util.List;

public interface AssistantToolService {

    List<UserDTO> searchDrivers(String name, String email, String phone, String role, String adminName);

    List<VehicleDTO> searchVehicles(String plateNumber, String brand, String model, String year, String adminName);

    List<MissionDTO> searchMissions(String status, String departureLocation, String destinationLocation, String driverName, String vehiclePlateNumber, String startDateFrom, String startDateTo, String endDateFrom, String endDateTo);

    List<DriverDocumentDTO> searchDriverDocuments(String title, String type, String status, String driverName, String driverId, String expiryDateFrom, String expiryDateTo);

    List<VehicleDocumentDTO> searchVehicleDocuments(String title, String type, String vehiclePlateNumber, String vehicleId, String expiryDateFrom, String expiryDateTo, String uploadedByName);

    List<MissionDocumentDTO> searchMissionDocuments(String title, String missionTitle, String missionId, String uploadedByName, String uploadedById);

    List<MissionVehicleInspectionDTO> searchInspections(String inspectionType, String inspectionDateFrom, String inspectionDateTo, String mileageFrom, String mileageTo, String fuelLevelFrom, String fuelLevelTo, String repairStatus, String accidentOccurred, String missionId, String vehiclePlateNumber);

}