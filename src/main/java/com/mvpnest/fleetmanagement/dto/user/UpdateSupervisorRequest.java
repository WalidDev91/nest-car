package com.mvpnest.fleetmanagement.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateSupervisorRequest {

    private UUID adminId;

}
