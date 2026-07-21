package com.mvpnest.fleetmanagement.dto.missiondocument;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionDocumentDTO {

    private UUID id;

    private String title;

    private String fileUrl;

    // mission
    private UUID missionId;
    private String missionTitle;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}