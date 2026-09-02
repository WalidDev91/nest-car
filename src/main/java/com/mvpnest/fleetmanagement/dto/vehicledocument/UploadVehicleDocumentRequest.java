package com.mvpnest.fleetmanagement.dto.vehicledocument;

import com.mvpnest.fleetmanagement.enums.VehicleDocumentType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadVehicleDocumentRequest {

    private String title;

    private VehicleDocumentType type;

    private LocalDate expiryDate;

    private UUID vehicleId;
}