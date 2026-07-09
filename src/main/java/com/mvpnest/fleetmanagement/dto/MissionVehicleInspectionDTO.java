package com.mvpnest.fleetmanagement.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionVehicleInspectionDTO {

    private UUID id;

    private LocalDateTime inspectionDate;

    private String notes;

    private Integer mileage;

    private Integer fuelLevel;

    // mission
    private UUID missionId;
    private String missionTitle;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<MissionVehiclePhotoDTO> photos;
}
