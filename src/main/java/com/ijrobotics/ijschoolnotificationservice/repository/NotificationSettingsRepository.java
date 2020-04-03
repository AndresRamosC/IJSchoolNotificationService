package com.ijrobotics.ijschoolnotificationservice.repository;

import com.ijrobotics.ijschoolnotificationservice.domain.NotificationSettings;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the NotificationSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {
    Optional<NotificationSettings> findByUseridId(long useridId);
}
