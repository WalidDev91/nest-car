package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.service.AssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantService assistantService;

    @PostMapping("/chat")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'FLEET_MANAGER')")
    public String chat(@RequestBody String message) {
        return assistantService.chat(message);
    }
}