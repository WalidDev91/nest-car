package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.auth.*;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    LoginOtpResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request, MultipartFile image);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    AuthResponse verifyOtp(VerifyOtpRequest request);

    void resendOtp(String email);

}