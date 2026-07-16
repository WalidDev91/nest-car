package com.mvpnest.fleetmanagement.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    private String token;

    private String newPassword;

}
