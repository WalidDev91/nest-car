package com.mvpnest.fleetmanagement.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginOtpResponse {

    private String message;
    private boolean requiresOtp;
}