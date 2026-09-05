package com.mvpnest.fleetmanagement.dto.userrequest;

import com.mvpnest.fleetmanagement.enums.RequestStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestRequest {

    private RequestStatus status;

    private String adminResponse;

}