package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.notification.NotificationDTO;
import com.mvpnest.fleetmanagement.entity.DriverDocument;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.MissionDocument;
import com.mvpnest.fleetmanagement.entity.UserRequest;
import com.mvpnest.fleetmanagement.entity.VehicleDocument;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<NotificationDTO> getMyNotifications(UUID userId);

    Long getUnreadCount(UUID userId);

    void markAsRead(UUID notificationId, UUID currentUserId);

    void markAllAsRead(UUID userId);

    void notifyDriverDocumentUploaded(DriverDocument document);

    void notifyDriverDocumentStatusChanged(DriverDocument document);

    void notifyVehicleDocumentUploaded(VehicleDocument document);

    void notifyMissionDocumentUploaded(MissionDocument document);

    void notifyMissionAssigned(Mission mission);

    void notifyVehicleAssigned(Mission mission);

    void notifyRequestSubmitted(UserRequest request);

    void notifyRequestReviewed(UserRequest request);

    void checkExpiringDocuments();

}