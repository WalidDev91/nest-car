package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.auth.LoginOtpResponse;
import com.mvpnest.fleetmanagement.dto.auth.LoginRequest;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.exception.InvalidCredentialsException;
import com.mvpnest.fleetmanagement.exception.ResourceNotFoundException;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    void login_withValidCredentials_sendsOtpAndReturnsOtpResponse() {

        LoginRequest request = new LoginRequest();
        request.setEmail("driver@example.com");
        request.setPassword("plainPassword");

        User user = User.builder().id(UUID.randomUUID()).firstName("Ali").lastName("Salem").email("driver@example.com").phone("+21612345678").password("hashedPassword").role(RoleType.DRIVER).build();

        when(userRepository.findByEmail("driver@example.com")).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);

        LoginOtpResponse response = authService.login(request);

        assertThat(response.getMessage()).isEqualTo("OTP sent successfully");

        assertThat(response.isRequiresOtp()).isTrue();

        verify(otpService).generateAndSendOtp(user);
    }


    @Test
    void login_withUnknownEmail_throwsResourceNotFoundException() {

        LoginRequest request = new LoginRequest();
        request.setEmail("ghost@example.com");
        request.setPassword("whatever");

        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request)).isInstanceOf(ResourceNotFoundException.class).hasMessage("User not found");

    }

    @Test
    void login_withWrongPassword_throwsInvalidCredentialsException() {

        LoginRequest request = new LoginRequest();
        request.setEmail("driver@example.com");
        request.setPassword("wrongPassword");

        User user = User.builder().email("driver@example.com").password("hashedPassword").role(RoleType.DRIVER).build();

        when(userRepository.findByEmail("driver@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request)).isInstanceOf(InvalidCredentialsException.class).hasMessage("Invalid password");

    }

}