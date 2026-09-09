package com.mvpnest.fleetmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvpnest.fleetmanagement.dto.auth.LoginOtpResponse;
import com.mvpnest.fleetmanagement.dto.auth.LoginRequest;
import com.mvpnest.fleetmanagement.security.JwtAuthenticationFilter;
import com.mvpnest.fleetmanagement.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_withValidRequestBody_returns200AndOtpResponse() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("driver@example.com");
        request.setPassword("plainPassword");

        LoginOtpResponse response = LoginOtpResponse.builder().message("OTP sent successfully").requiresOtp(true).build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("OTP sent successfully")).andExpect(jsonPath("$.requiresOtp").value(true));

    }

}