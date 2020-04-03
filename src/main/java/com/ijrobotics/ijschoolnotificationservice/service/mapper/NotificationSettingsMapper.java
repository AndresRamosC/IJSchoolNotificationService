package com.ijrobotics.ijschoolnotificationservice.service.mapper;


import com.ijrobotics.ijschoolnotificationservice.domain.*;
import com.ijrobotics.ijschoolnotificationservice.service.dto.NotificationSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationSettings} and its DTO {@link NotificationSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {UseridMapper.class})
public interface NotificationSettingsMapper extends EntityMapper<NotificationSettingsDTO, NotificationSettings> {

    @Mapping(source = "userid.id", target = "useridId")
    NotificationSettingsDTO toDto(NotificationSettings notificationSettings);

    @Mapping(source = "useridId", target = "userid")
    NotificationSettings toEntity(NotificationSettingsDTO notificationSettingsDTO);

    default NotificationSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        NotificationSettings notificationSettings = new NotificationSettings();
        notificationSettings.setId(id);
        return notificationSettings;
    }
}
