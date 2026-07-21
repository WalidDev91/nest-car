package com.mvpnest.fleetmanagement.dto.driverdocument;

import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDriverDocumentRequest {


    private String title;


    private DriverDocumentType type;

}