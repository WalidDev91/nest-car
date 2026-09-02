package com.mvpnest.fleetmanagement.dto.vehicledocument;

import com.mvpnest.fleetmanagement.enums.VehicleDocumentType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVehicleDocumentRequest {

    private String title;

    private VehicleDocumentType type;

    private LocalDate expiryDate;

}