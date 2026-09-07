package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.notification.NotificationDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDTO> getMine(Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        return notificationService.getMyNotifications(currentUser.getId());

    }

    @GetMapping("/unread-count")
    public Long getUnreadCount(Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        return notificationService.getUnreadCount(currentUser.getId());

    }

    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable UUID id, Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        notificationService.markAsRead(id, currentUser.getId());

    }

    @PatchMapping("/read-all")
    public void markAllAsRead(Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        notificationService.markAllAsRead(currentUser.getId());

    }

}