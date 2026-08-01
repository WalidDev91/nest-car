package com.mvpnest.fleetmanagement.dto.mission;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionInspectionRequest {

    private Integer mileage;

    private Integer fuelLevel;

    private String notes;

}
