package com.mvpnest.fleetmanagement.dto.userrequest;

import com.mvpnest.fleetmanagement.enums.RequestType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRequestRequest {

    private RequestType type;

    private String subject;

    private String description;

}