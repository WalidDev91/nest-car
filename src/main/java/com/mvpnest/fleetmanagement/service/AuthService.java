package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.auth.AuthResponse;
import com.mvpnest.fleetmanagement.dto.auth.LoginRequest;
import com.mvpnest.fleetmanagement.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

}
