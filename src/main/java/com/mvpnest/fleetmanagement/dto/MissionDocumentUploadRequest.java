package com.mvpnest.fleetmanagement.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MissionDocumentUploadRequest {

    private String title;
    private UUID missionId;
}
