package com.mvpnest.fleetmanagement.dto.vehicle;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleUpdateRequest {

    private String plateNumber;

    private String brand;

    private String model;

    private Integer year;

    private java.util.UUID adminId;
}