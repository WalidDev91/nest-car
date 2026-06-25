package com.mvpnest.fleetmanagement.dto;

import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import lombok.Data;

import java.util.UUID;

@Data
public class DriverDocumentUploadRequest {

    private String title;
    private DriverDocumentType type;
    private UUID driverId;
}
