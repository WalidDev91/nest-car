package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.auth.OtpResponse;
import com.mvpnest.fleetmanagement.dto.auth.VerifyOtpRequest;
import com.mvpnest.fleetmanagement.entity.User;

public interface OtpService {

    OtpResponse generateAndSendOtp(User user);

    User verifyOtp(VerifyOtpRequest request);
}