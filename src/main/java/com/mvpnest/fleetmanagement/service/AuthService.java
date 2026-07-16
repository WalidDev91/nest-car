package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.auth.*;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

}
