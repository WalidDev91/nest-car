package com.mvpnest.fleetmanagement.dto.user;

import com.mvpnest.fleetmanagement.enums.RoleType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private RoleType role;

    private boolean isValidate;

    private UUID adminId;
}
