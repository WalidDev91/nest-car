package com.mvpnest.fleetmanagement.dto.driverdocument;

import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDriverDocumentStatusRequest {

    private DriverDocumentStatus status;
}
