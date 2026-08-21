package com.mvpnest.fleetmanagement.dto.vehicledocument;

import com.mvpnest.fleetmanagement.enums.VehicleDocumentType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDocumentDTO {

    private UUID id;

    private String title;

    private VehicleDocumentType type;

    private String fileUrl;

    private Integer year;

    // vehicle
    private UUID vehicleId;
    private String vehiclePlateNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
