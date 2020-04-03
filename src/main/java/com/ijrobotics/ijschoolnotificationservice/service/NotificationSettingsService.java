package com.ijrobotics.ijschoolnotificationservice.service;

import com.ijrobotics.ijschoolnotificationservice.domain.NotificationSettings;
import com.ijrobotics.ijschoolnotificationservice.repository.NotificationSettingsRepository;
import com.ijrobotics.ijschoolnotificationservice.service.dto.NotificationSettingsDTO;
import com.ijrobotics.ijschoolnotificationservice.service.mapper.NotificationSettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link NotificationSettings}.
 */
@Service
@Transactional
public class NotificationSettingsService {

    private final Logger log = LoggerFactory.getLogger(NotificationSettingsService.class);

    private final NotificationSettingsRepository notificationSettingsRepository;

    private final NotificationSettingsMapper notificationSettingsMapper;

    public NotificationSettingsService(NotificationSettingsRepository notificationSettingsRepository, NotificationSettingsMapper notificationSettingsMapper) {
        this.notificationSettingsRepository = notificationSettingsRepository;
        this.notificationSettingsMapper = notificationSettingsMapper;
    }

    /**
     * Save a notificationSettings.
     *
     * @param notificationSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationSettingsDTO save(NotificationSettingsDTO notificationSettingsDTO) {
        log.debug("Request to save NotificationSettings : {}", notificationSettingsDTO);
        NotificationSettings notificationSettings = notificationSettingsMapper.toEntity(notificationSettingsDTO);
        notificationSettings = notificationSettingsRepository.save(notificationSettings);
        return notificationSettingsMapper.toDto(notificationSettings);
    }

    /**
     * Get all the notificationSettings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NotificationSettingsDTO> findAll() {
        log.debug("Request to get all NotificationSettings");
        return notificationSettingsRepository.findAll().stream()
            .map(notificationSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one notificationSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationSettingsDTO> findOne(Long id) {
        log.debug("Request to get NotificationSettings : {}", id);
        return notificationSettingsRepository.findById(id)
            .map(notificationSettingsMapper::toDto);
    }
    /**
     * Get one notificationSettings by userIdId.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationSettingsDTO> findOneByUserId(Long id) {
        log.debug("Request to get NotificationSettings : {}", id);
        return notificationSettingsRepository.findById(id)
            .map(notificationSettingsMapper::toDto);
    }

    /**
     * Delete the notificationSettings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NotificationSettings : {}", id);
        notificationSettingsRepository.deleteById(id);
    }
}
