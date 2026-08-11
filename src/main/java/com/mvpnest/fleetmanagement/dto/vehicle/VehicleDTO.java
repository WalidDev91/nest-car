package com.mvpnest.fleetmanagement.dto.vehicle;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {

    private UUID id;

    private String plateNumber;

    private String brand;

    private String model;

    private Integer year;

    // admin responsible for vehicle
    private UUID adminId;
    private String adminName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<VehiclePhotoDTO> photos;
}
