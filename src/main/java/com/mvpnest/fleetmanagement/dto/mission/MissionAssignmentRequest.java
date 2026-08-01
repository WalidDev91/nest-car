package com.mvpnest.fleetmanagement.dto.mission;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionAssignmentRequest {

    private UUID driverId;

    private UUID vehicleId;

}
