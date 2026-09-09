package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.auth.OtpResponse;
import com.mvpnest.fleetmanagement.dto.auth.VerifyOtpRequest;
import com.mvpnest.fleetmanagement.entity.LoginOtp;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.repository.LoginOtpRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.OtpService;
import com.mvpnest.fleetmanagement.service.TextBeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final LoginOtpRepository loginOtpRepository;
    private final TextBeeService textBeeService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OtpResponse generateAndSendOtp(User user) {

        loginOtpRepository.deleteByUser(user);

        String code = String.format("%06d", new Random().nextInt(1_000_000));

        LoginOtp otp = LoginOtp.builder().user(user).code(code).expiresAt(LocalDateTime.now().plusMinutes(5)).build();

        loginOtpRepository.save(otp);

        textBeeService.sendSms(user.getPhone(), "Your Nest-Car login code is: " + code);

        return OtpResponse.builder().message("OTP sent successfully").build();
    }

    @Override
    @Transactional
    public User verifyOtp(VerifyOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        LoginOtp otp = loginOtpRepository.findByUserAndCode(user, request.getCode()).orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        loginOtpRepository.delete(otp);

        return user;
    }

}