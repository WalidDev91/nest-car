package com.mvpnest.fleetmanagement.dto;

import com.mvpnest.fleetmanagement.enums.VehicleType;
import lombok.Data;

import java.util.UUID;

@Data
public class VehicleDocumentUploadRequest {

    private String title;
    private VehicleType type;
    private Integer year;
    private UUID vehicleId;
}
