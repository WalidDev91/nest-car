package com.mvpnest.fleetmanagement.dto.driverdocument;

import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadDriverDocumentRequest {


    private String title;


    private DriverDocumentType type;


    private UUID driverId;

}