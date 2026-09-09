package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.LoginOtp;
import com.mvpnest.fleetmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoginOtpRepository extends JpaRepository<LoginOtp, UUID> {

    Optional<LoginOtp> findTopByUserOrderByExpiresAtDesc(User user);

    Optional<LoginOtp> findByUserAndCode(User user, String code);

    void deleteByUser(User user);
}