package com.mvpnest.fleetmanagement.dto;

import com.mvpnest.fleetmanagement.enums.RoleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private RoleType role;

}
