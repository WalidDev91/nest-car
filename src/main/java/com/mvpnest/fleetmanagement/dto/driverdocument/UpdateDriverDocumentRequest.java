package com.mvpnest.fleetmanagement.dto.driverdocument;

import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDriverDocumentRequest {

    private String title;

    private LocalDate expiryDate;

    private DriverDocumentType type;

}