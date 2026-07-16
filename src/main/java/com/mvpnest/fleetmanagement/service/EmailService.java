package com.mvpnest.fleetmanagement.service;

public interface EmailService {

    void sendResetPasswordEmail(String to, String token);

}
