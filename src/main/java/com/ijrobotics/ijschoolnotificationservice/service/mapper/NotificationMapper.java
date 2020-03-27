package com.ijrobotics.ijschoolnotificationservice.service.mapper;


import com.ijrobotics.ijschoolnotificationservice.domain.*;
import com.ijrobotics.ijschoolnotificationservice.service.dto.NotificationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = {UseridMapper.class})
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {

    @Mapping(source = "keycloakUserId.id", target = "keycloakUserIdId")
    NotificationDTO toDto(Notification notification);

    @Mapping(source = "keycloakUserIdId", target = "keycloakUserId")
    Notification toEntity(NotificationDTO notificationDTO);

    default Notification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setId(id);
        return notification;
    }
}
