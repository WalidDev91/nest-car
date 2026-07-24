package com.mvpnest.fleetmanagement.dto.auth;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private UUID id;

    private String token;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String phone;
}
