package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.notification.NotificationDTO;
import com.mvpnest.fleetmanagement.entity.*;
import com.mvpnest.fleetmanagement.enums.NotificationType;
import com.mvpnest.fleetmanagement.mapper.NotificationMapper;
import com.mvpnest.fleetmanagement.repository.DriverDocumentRepository;
import com.mvpnest.fleetmanagement.repository.NotificationRepository;
import com.mvpnest.fleetmanagement.repository.VehicleDocumentRepository;
import com.mvpnest.fleetmanagement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;
    private final DriverDocumentRepository driverDocumentRepository;
    private final VehicleDocumentRepository vehicleDocumentRepository;

    private static final int EXPIRY_REMINDER_DAYS_AHEAD = 7;

    // =====================================================
    // READ / MANAGE
    // =====================================================

    @Override
    public List<NotificationDTO> getMyNotifications(UUID userId) {

        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId).stream()
                .map(mapper::toDTO)
                .toList();

    }

    @Override
    public Long getUnreadCount(UUID userId) {

        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);

    }

    @Override
    public void markAsRead(UUID notificationId, UUID currentUserId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getRecipient().getId().equals(currentUserId)) {
            throw new RuntimeException("You cannot modify another user's notification");
        }

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());

        notificationRepository.save(notification);

    }

    @Override
    public void markAllAsRead(UUID userId) {

        List<Notification> unread = notificationRepository.findByRecipientIdAndIsReadFalse(userId);

        LocalDateTime now = LocalDateTime.now();

        unread.forEach(n -> {
            n.setIsRead(true);
            n.setReadAt(now);
        });

        notificationRepository.saveAll(unread);

    }

    // =====================================================
    // INTERNAL HELPER
    // =====================================================

    private void createNotification(User recipient, NotificationType type, String title, String message, String link) {

        if (recipient == null) return;

        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(type)
                .title(title)
                .message(message)
                .link(link)
                .build();

        notificationRepository.save(notification);

    }

    // =====================================================
    // TRIGGERS — DRIVER DOCUMENTS
    // =====================================================

    @Override
    public void notifyDriverDocumentUploaded(DriverDocument document) {

        User driver = document.getDriver();

        if (driver == null || driver.getAdmin() == null) return;

        createNotification(
                driver.getAdmin(),
                NotificationType.DOCUMENT_UPLOADED,
                "New document uploaded",
                driver.getFirstName() + " " + driver.getLastName() + " uploaded a new document: " + document.getTitle(),
                "/documents"
        );

    }

    @Override
    public void notifyDriverDocumentStatusChanged(DriverDocument document) {

        User driver = document.getDriver();

        if (driver == null) return;

        boolean approved = document.getStatus() == com.mvpnest.fleetmanagement.enums.DriverDocumentStatus.APPROVED;
        boolean rejected = document.getStatus() == com.mvpnest.fleetmanagement.enums.DriverDocumentStatus.REJECTED;

        if (!approved && !rejected) return;

        createNotification(
                driver,
                approved ? NotificationType.DOCUMENT_APPROVED : NotificationType.DOCUMENT_REJECTED,
                approved ? "Document approved" : "Document rejected",
                "Your document \"" + document.getTitle() + "\" was " + (approved ? "approved" : "rejected") + ".",
                "/profile"
        );

    }

    // =====================================================
    // TRIGGERS — VEHICLE DOCUMENTS
    // =====================================================

    @Override
    public void notifyVehicleDocumentUploaded(VehicleDocument document) {

        User uploader = document.getUploadedBy();

        if (uploader == null || uploader.getAdmin() == null) return;

        createNotification(
                uploader.getAdmin(),
                NotificationType.DOCUMENT_UPLOADED,
                "New vehicle document uploaded",
                uploader.getFirstName() + " " + uploader.getLastName() + " uploaded a vehicle document: " + document.getTitle(),
                "/documents"
        );

    }

    // =====================================================
    // TRIGGERS — MISSION DOCUMENTS
    // =====================================================

    @Override
    public void notifyMissionDocumentUploaded(MissionDocument document) {

        User uploader = document.getUploadedBy();

        if (uploader == null || uploader.getAdmin() == null) return;

        createNotification(
                uploader.getAdmin(),
                NotificationType.DOCUMENT_UPLOADED,
                "New mission document uploaded",
                uploader.getFirstName() + " " + uploader.getLastName() + " uploaded a mission document: " + document.getTitle(),
                "/documents"
        );

    }

    // =====================================================
    // TRIGGERS — ASSIGNMENT
    // =====================================================

    @Override
    public void notifyMissionAssigned(Mission mission) {

        User driver = mission.getDriver();

        if (driver == null) return;

        createNotification(
                driver,
                NotificationType.MISSION_ASSIGNED,
                "New mission assigned",
                "You have been assigned to mission: " + mission.getTitle(),
                "/missions"
        );

    }

    @Override
    public void notifyVehicleAssigned(Mission mission) {

        User driver = mission.getDriver();

        if (driver == null || mission.getVehicle() == null) return;

        createNotification(
                driver,
                NotificationType.VEHICLE_ASSIGNED,
                "Vehicle assigned",
                "Vehicle " + mission.getVehicle().getPlateNumber() + " has been assigned to your mission: " + mission.getTitle(),
                "/missions"
        );

    }

    // =====================================================
    // TRIGGERS — REQUESTS
    // =====================================================

    @Override
    public void notifyRequestSubmitted(UserRequest request) {

        User requester = request.getRequester();

        if (requester == null || requester.getAdmin() == null) return;

        createNotification(
                requester.getAdmin(),
                NotificationType.REQUEST_SUBMITTED,
                "New request submitted",
                requester.getFirstName() + " " + requester.getLastName() + " submitted a request: " + request.getSubject(),
                "/administration"
        );

    }

    @Override
    public void notifyRequestReviewed(UserRequest request) {

        User requester = request.getRequester();

        if (requester == null) return;

        createNotification(
                requester,
                NotificationType.REQUEST_REVIEWED,
                "Your request was reviewed",
                "Your request \"" + request.getSubject() + "\" was updated to: " + request.getStatus(),
                "/profile"
        );

    }

    // =====================================================
    // SCHEDULED — EXPIRY REMINDERS
    // =====================================================

    @Override
    @Scheduled(cron = "0 0 8 * * *") // every day at 08:00
    public void checkExpiringDocuments() {

        LocalDate threshold = LocalDate.now().plusDays(EXPIRY_REMINDER_DAYS_AHEAD);

        driverDocumentRepository.findAll().stream()
                .filter(doc -> doc.getExpiryDate() != null && !doc.getExpiryDate().isAfter(threshold) && !doc.getExpiryDate().isBefore(LocalDate.now()))
                .forEach(doc -> remindIfNotAlreadySent(doc.getDriver(), NotificationType.DOCUMENT_EXPIRING_SOON,
                        "Document expiring soon",
                        "Your document \"" + doc.getTitle() + "\" expires on " + doc.getExpiryDate() + ".",
                        "/profile"));

        vehicleDocumentRepository.findAll().stream()
                .filter(doc -> doc.getExpiryDate() != null && !doc.getExpiryDate().isAfter(threshold) && !doc.getExpiryDate().isBefore(LocalDate.now()))
                .forEach(doc -> remindIfNotAlreadySent(doc.getUploadedBy(), NotificationType.DOCUMENT_EXPIRING_SOON,
                        "Vehicle document expiring soon",
                        "Vehicle document \"" + doc.getTitle() + "\" expires on " + doc.getExpiryDate() + ".",
                        "/documents"));

    }

    private void remindIfNotAlreadySent(User recipient, NotificationType type, String title, String message, String link) {

        if (recipient == null) return;

        // Don't spam — skip if a reminder for this exact link was already sent in the last 24h.
        List<Notification> recent = notificationRepository.findByLinkAndTypeAndCreatedAtAfter(
                link, type, LocalDateTime.now().minusHours(24)
        );

        boolean alreadySentToThisUser = recent.stream()
                .anyMatch(n -> n.getRecipient().getId().equals(recipient.getId()) && n.getMessage().equals(message));

        if (alreadySentToThisUser) return;

        createNotification(recipient, type, title, message, link);

    }

}