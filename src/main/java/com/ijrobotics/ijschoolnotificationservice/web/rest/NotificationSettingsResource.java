package com.ijrobotics.ijschoolnotificationservice.web.rest;

import com.ijrobotics.ijschoolnotificationservice.service.NotificationSettingsService;
import com.ijrobotics.ijschoolnotificationservice.web.rest.errors.BadRequestAlertException;
import com.ijrobotics.ijschoolnotificationservice.service.dto.NotificationSettingsDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ijrobotics.ijschoolnotificationservice.domain.NotificationSettings}.
 */
@RestController
@RequestMapping("/api")
public class NotificationSettingsResource {

    private final Logger log = LoggerFactory.getLogger(NotificationSettingsResource.class);

    private static final String ENTITY_NAME = "ijSchoolManagerNotificationServiceNotificationSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationSettingsService notificationSettingsService;

    public NotificationSettingsResource(NotificationSettingsService notificationSettingsService) {
        this.notificationSettingsService = notificationSettingsService;
    }

    /**
     * {@code POST  /notification-settings} : Create a new notificationSettings.
     *
     * @param notificationSettingsDTO the notificationSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationSettingsDTO, or with status {@code 400 (Bad Request)} if the notificationSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notification-settings")
    public ResponseEntity<NotificationSettingsDTO> createNotificationSettings(@RequestBody NotificationSettingsDTO notificationSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save NotificationSettings : {}", notificationSettingsDTO);
        if (notificationSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationSettingsDTO result = notificationSettingsService.save(notificationSettingsDTO);
        return ResponseEntity.created(new URI("/api/notification-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notification-settings} : Updates an existing notificationSettings.
     *
     * @param notificationSettingsDTO the notificationSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the notificationSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notification-settings")
    public ResponseEntity<NotificationSettingsDTO> updateNotificationSettings(@RequestBody NotificationSettingsDTO notificationSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update NotificationSettings : {}", notificationSettingsDTO);
        if (notificationSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotificationSettingsDTO result = notificationSettingsService.save(notificationSettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationSettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notification-settings} : get all the notificationSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationSettings in body.
     */
    @GetMapping("/notification-settings")
    public List<NotificationSettingsDTO> getAllNotificationSettings() {
        log.debug("REST request to get all NotificationSettings");
        return notificationSettingsService.findAll();
    }

    /**
     * {@code GET  /notification-settings/:id} : get the "id" notificationSettings.
     *
     * @param id the id of the notificationSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notification-settings/{id}")
    public ResponseEntity<NotificationSettingsDTO> getNotificationSettings(@PathVariable Long id) {
        log.debug("REST request to get NotificationSettings : {}", id);
        Optional<NotificationSettingsDTO> notificationSettingsDTO = notificationSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationSettingsDTO);
    }

    /**
     * {@code DELETE  /notification-settings/:id} : delete the "id" notificationSettings.
     *
     * @param id the id of the notificationSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notification-settings/{id}")
    public ResponseEntity<Void> deleteNotificationSettings(@PathVariable Long id) {
        log.debug("REST request to delete NotificationSettings : {}", id);
        notificationSettingsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
