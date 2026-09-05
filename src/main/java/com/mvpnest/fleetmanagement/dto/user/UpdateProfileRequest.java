package com.mvpnest.fleetmanagement.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

}