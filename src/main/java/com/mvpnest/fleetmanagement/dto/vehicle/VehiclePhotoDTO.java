package com.mvpnest.fleetmanagement.dto.vehicle;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiclePhotoDTO {

    private UUID id;
    private String photoUrl;
    private String description;
}