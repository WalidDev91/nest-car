package com.mvpnest.fleetmanagement.dto.missionvehicleinspection;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMissionVehicleInspectionRequest {

    private UUID missionId;

    private String notes;

    private Integer mileage;

    private Integer fuelLevel;

}