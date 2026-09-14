package com.mvpnest.fleetmanagement.dto.missionvehicleinspection;

import com.mvpnest.fleetmanagement.enums.InspectionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionInspectionRequest {

    private InspectionType inspectionType;

    private Integer mileage;

    private Integer fuelLevel;

    private String tirePressure;

    private String oilChange;

    private String waterCheck;

    private String partsCondition;

    private String repairStatus;

    private Boolean accidentOccurred;

    private String notes;
}