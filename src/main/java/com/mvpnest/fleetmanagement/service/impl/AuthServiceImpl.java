package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.auth.AuthResponse;
import com.mvpnest.fleetmanagement.dto.auth.LoginRequest;
import com.mvpnest.fleetmanagement.dto.auth.RegisterRequest;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.security.JwtService;
import com.mvpnest.fleetmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mvpnest.fleetmanagement.dto.auth.ForgotPasswordRequest;
import com.mvpnest.fleetmanagement.dto.auth.ResetPasswordRequest;
import com.mvpnest.fleetmanagement.service.EmailService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    // ================== LOGIN ==================
    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    // ================== REGISTER ==================
    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleType.DRIVER)
                .isValidate(true)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        String token = UUID.randomUUID().toString();


        user.setResetToken(token);

        user.setResetTokenExpiration(LocalDateTime.now().plusMinutes(5));


        userRepository.save(user);


        emailService.sendResetPasswordEmail(
                user.getEmail(),
                token
        );
    }


    @Override
    public void resetPassword(ResetPasswordRequest request) {


        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() ->
                        new RuntimeException("Invalid token")
                );


        if(user.getResetTokenExpiration()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("Token expired");

        }


        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );


        user.setResetToken(null);
        user.setResetTokenExpiration(null);


        userRepository.save(user);

    }

}
