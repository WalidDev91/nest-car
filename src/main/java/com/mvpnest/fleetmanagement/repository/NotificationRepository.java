package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.Notification;
import com.mvpnest.fleetmanagement.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId);

    List<Notification> findByRecipientIdAndIsReadFalse(UUID recipientId);

    Long countByRecipientIdAndIsReadFalse(UUID recipientId);

    List<Notification> findByLinkAndTypeAndCreatedAtAfter(String link, NotificationType type, LocalDateTime after);

}