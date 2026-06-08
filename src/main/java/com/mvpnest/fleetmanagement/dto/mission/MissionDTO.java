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
public class MissionDTO {

    private UUID id;

    private String title;

    private String description;

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
}
