package com.ijrobotics.ijschoolnotificationservice.repository;

import com.ijrobotics.ijschoolnotificationservice.domain.Userid;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Userid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UseridRepository extends JpaRepository<Userid, Long> {
    Optional<Userid> findByKeycloakUserId(String keycloakUserId);
}
