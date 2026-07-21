package com.mvpnest.fleetmanagement.dto.missionvehiclephoto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadMissionVehiclePhotoRequest {

    private String description;

    private UUID inspectionId;

}
