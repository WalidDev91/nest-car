package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.auth.*;
import com.mvpnest.fleetmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<AuthResponse> register(@RequestPart("user") RegisterRequest request, @RequestPart(value = "image", required = false) MultipartFile image) {

        return ResponseEntity.ok(authService.register(request, image));

    }

    @PostMapping("/login")
    public ResponseEntity<LoginOtpResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {

        authService.resendOtp(email);

        return ResponseEntity.ok("OTP resent successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok("Email sent");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password updated");
    }

}