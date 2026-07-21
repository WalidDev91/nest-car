package com.mvpnest.fleetmanagement.dto.missionvehicleinspection;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMissionVehicleInspectionRequest {

    private String notes;

    private Integer mileage;

    private Integer fuelLevel;

}
