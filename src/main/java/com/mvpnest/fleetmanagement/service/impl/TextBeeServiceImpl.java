package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.service.TextBeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TextBeeServiceImpl implements TextBeeService {

    private static final String TEXTBEE_URL = "https://api.textbee.dev/api/v1/gateway/send-sms";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${textbee.api-key}")
    private String apiKey;

    @Value("${textbee.device-id}")
    private String deviceId;

    @Override
    public void sendSms(String phoneNumber, String message) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);

        Map<String, Object> body = Map.of("recipients", new String[]{phoneNumber}, "message", message, "deviceId", deviceId);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(TEXTBEE_URL, request, String.class);
    }
}