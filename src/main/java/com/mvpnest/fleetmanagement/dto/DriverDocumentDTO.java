package com.mvpnest.fleetmanagement.dto;

import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDocumentDTO {

    private UUID id;

    private String title;

    private DriverDocumentType type;

    private String fileUrl;

    private DriverDocumentStatus status;

    private LocalDateTime uploadedAt;

    private LocalDateTime validatedAt;

    // driver
    private UUID driverId;
    private String driverName;

    // audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
