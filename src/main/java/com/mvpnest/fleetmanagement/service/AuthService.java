package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.AuthResponse;
import com.mvpnest.fleetmanagement.dto.LoginRequest;
import com.mvpnest.fleetmanagement.dto.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

}
