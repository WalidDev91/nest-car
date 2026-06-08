package com.mvpnest.fleetmanagement.dto.vehicle;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCreateRequest {

    private String plateNumber;

    private String brand;

    private String model;

    private Integer year;

    // admin who creates/manages vehicle
    private java.util.UUID adminId;
}
