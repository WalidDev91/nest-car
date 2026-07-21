package com.mvpnest.fleetmanagement.dto.missiondocument;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadMissionDocumentRequest {

    private String title;

    private UUID missionId;

}
