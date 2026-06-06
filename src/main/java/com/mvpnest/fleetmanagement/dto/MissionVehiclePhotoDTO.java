package com.mvpnest.fleetmanagement.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionVehiclePhotoDTO {

    private UUID id;

    private String photoUrl;

    private String description;

    private LocalDateTime takenAt;

    // inspection
    private UUID inspectionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}