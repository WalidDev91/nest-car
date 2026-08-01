package com.mvpnest.fleetmanagement.dto.mission;

import com.mvpnest.fleetmanagement.enums.MissionStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMissionRequest {

    private String title;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private MissionStatus status;

    private UUID driverId;

    private UUID vehicleId;

}