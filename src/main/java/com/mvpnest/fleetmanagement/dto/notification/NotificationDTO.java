package com.mvpnest.fleetmanagement.dto.notification;

import com.mvpnest.fleetmanagement.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private UUID id;

    private NotificationType type;

    private String title;

    private String message;

    private String link;

    private Boolean isRead;

    private LocalDateTime createdAt;

}