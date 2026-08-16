package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.auth.*;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.exception.InvalidCredentialsException;
import com.mvpnest.fleetmanagement.exception.ResourceNotFoundException;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.security.JwtService;
import com.mvpnest.fleetmanagement.service.AuthService;
import com.mvpnest.fleetmanagement.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder().id(user.getId()).token(token).email(user.getEmail()).phone(user.getPhone()).role(user.getRole().name()).firstName(user.getFirstName()).lastName(user.getLastName()).imageUrl(user.getImageUrl()).build();

    }

    @Override
    public AuthResponse register(RegisterRequest request, MultipartFile image) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User admin = null;

        if (request.getAdminId() != null) {
            admin = userRepository.findById(request.getAdminId()).orElseThrow(() -> new RuntimeException("Admin not found"));
        }

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {

            try {

                Path folder = Paths.get(uploadDir, "users");
                Files.createDirectories(folder);

                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();

                Path path = folder.resolve(filename);

                Files.copy(image.getInputStream(), path);

                imageUrl = filename;

            } catch (IOException e) {
                throw new RuntimeException("Image upload failed");
            }

        }

        User user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName()).email(request.getEmail()).phone(request.getPhone()).password(passwordEncoder.encode(request.getPassword())).role(RoleType.DRIVER).admin(admin).isValidate(true).imageUrl(imageUrl).build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder().id(user.getId()).token(token).email(user.getEmail()).phone(user.getPhone()).role(user.getRole().name()).firstName(user.getFirstName()).lastName(user.getLastName()).imageUrl(user.getImageUrl()).build();

    }

    // ================== FORGOT PASSWORD ==================
    @Override
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiration(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        emailService.sendResetPasswordEmail(user.getEmail(), token);
    }

    // ================== RESET PASSWORD ==================
    @Override
    public void resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByResetToken(request.getToken()).orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiration(null);

        userRepository.save(user);
    }
}