package com.mvpnest.fleetmanagement.dto.missionvehicleinspection;

import com.mvpnest.fleetmanagement.dto.missionvehiclephoto.MissionVehiclePhotoDTO;
import com.mvpnest.fleetmanagement.enums.InspectionType;
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

    private InspectionType inspectionType;

    private LocalDateTime inspectionDate;

    private Integer mileage;

    private Integer fuelLevel;

    private String tirePressure;

    private String oilChange;

    private String waterCheck;

    private String partsCondition;

    private String repairStatus;

    private Boolean accidentOccurred;

    private String notes;

    private List<UUID> photoIds;

    private List<MissionVehiclePhotoDTO> photos;
}