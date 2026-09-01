package com.mvpnest.fleetmanagement.dto.mission;

import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionDTO {

    private UUID id;

    private String title;

    private String description;

    private String departureLocation;

    private String destinationLocation;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private MissionStatus status;


    // driver
    private UUID driverId;
    private String driverName;


    // vehicle
    private UUID vehicleId;
    private String vehiclePlateNumber;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // inspection
    private MissionVehicleInspectionDTO vehicleInspection;

    // documents verification
    private Boolean documentsVerified;

    private LocalDateTime documentsVerificationDate;

    // mission documents
    private List<MissionDocumentDTO> documents;
}