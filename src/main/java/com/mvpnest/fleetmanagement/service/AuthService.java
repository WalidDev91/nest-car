package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.AuthResponse;
import com.mvpnest.fleetmanagement.dto.LoginRequest;
import com.mvpnest.fleetmanagement.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}
