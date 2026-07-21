package com.mvpnest.fleetmanagement.dto.vehicledocument;

import com.mvpnest.fleetmanagement.enums.VehicleType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadVehicleDocumentRequest {

    private String title;

    private VehicleType type;

    private Integer year;

    private UUID vehicleId;
}