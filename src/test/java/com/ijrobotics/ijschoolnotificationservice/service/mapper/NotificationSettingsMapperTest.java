package com.ijrobotics.ijschoolnotificationservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationSettingsMapperTest {

    private NotificationSettingsMapper notificationSettingsMapper;

    @BeforeEach
    public void setUp() {
        notificationSettingsMapper = new NotificationSettingsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(notificationSettingsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(notificationSettingsMapper.fromId(null)).isNull();
    }
}
