package com.mvpnest.fleetmanagement.dto.vehicledocument;

import com.mvpnest.fleetmanagement.enums.VehicleDocumentType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVehicleDocumentRequest {

    private String title;

    private VehicleDocumentType type;

    private Integer year;

}