package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.notification.NotificationDTO;
import com.mvpnest.fleetmanagement.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDTO toDTO(Notification notification);

}