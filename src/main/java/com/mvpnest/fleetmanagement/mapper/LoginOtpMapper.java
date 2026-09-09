package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.auth.OtpResponse;
import com.mvpnest.fleetmanagement.entity.LoginOtp;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginOtpMapper {

    OtpResponse toResponse(LoginOtp loginOtp);

}