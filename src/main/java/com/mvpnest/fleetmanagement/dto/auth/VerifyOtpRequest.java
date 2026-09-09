package com.mvpnest.fleetmanagement.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyOtpRequest {

    private String email;
    private String code;
}