package com.ijrobotics.ijschoolnotificationservice.web.rest;

import com.ijrobotics.ijschoolnotificationservice.IjSchoolManagerNotificationServiceApp;
import com.ijrobotics.ijschoolnotificationservice.config.TestSecurityConfiguration;
import com.ijrobotics.ijschoolnotificationservice.domain.NotificationSettings;
import com.ijrobotics.ijschoolnotificationservice.repository.NotificationSettingsRepository;
import com.ijrobotics.ijschoolnotificationservice.service.NotificationSettingsService;
import com.ijrobotics.ijschoolnotificationservice.service.dto.NotificationSettingsDTO;
import com.ijrobotics.ijschoolnotificationservice.service.mapper.NotificationSettingsMapper;
import com.ijrobotics.ijschoolnotificationservice.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.ijrobotics.ijschoolnotificationservice.web.rest.TestUtil.sameInstant;
import static com.ijrobotics.ijschoolnotificationservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link NotificationSettingsResource} REST controller.
 */
@SpringBootTest(classes = {IjSchoolManagerNotificationServiceApp.class, TestSecurityConfiguration.class})
public class NotificationSettingsResourceIT {

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ATTENDANCE = false;
    private static final Boolean UPDATED_ATTENDANCE = true;

    private static final Boolean DEFAULT_HOMEWORK = false;
    private static final Boolean UPDATED_HOMEWORK = true;

    private static final Boolean DEFAULT_NOTICE = false;
    private static final Boolean UPDATED_NOTICE = true;

    @Autowired
    private NotificationSettingsRepository notificationSettingsRepository;

    @Autowired
    private NotificationSettingsMapper notificationSettingsMapper;

    @Autowired
    private NotificationSettingsService notificationSettingsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restNotificationSettingsMockMvc;

    private NotificationSettings notificationSettings;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationSettingsResource notificationSettingsResource = new NotificationSettingsResource(notificationSettingsService);
        this.restNotificationSettingsMockMvc = MockMvcBuilders.standaloneSetup(notificationSettingsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationSettings createEntity(EntityManager em) {
        NotificationSettings notificationSettings = new NotificationSettings()
            .creationDate(DEFAULT_CREATION_DATE)
            .attendance(DEFAULT_ATTENDANCE)
            .homework(DEFAULT_HOMEWORK)
            .notice(DEFAULT_NOTICE);
        return notificationSettings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationSettings createUpdatedEntity(EntityManager em) {
        NotificationSettings notificationSettings = new NotificationSettings()
            .creationDate(UPDATED_CREATION_DATE)
            .attendance(UPDATED_ATTENDANCE)
            .homework(UPDATED_HOMEWORK)
            .notice(UPDATED_NOTICE);
        return notificationSettings;
    }

    @BeforeEach
    public void initTest() {
        notificationSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationSettings() throws Exception {
        int databaseSizeBeforeCreate = notificationSettingsRepository.findAll().size();

        // Create the NotificationSettings
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);
        restNotificationSettingsMockMvc.perform(post("/api/notification-settings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationSettingsDTO)))
            .andExpect(status().isCreated());

        // Validate the NotificationSettings in the database
        List<NotificationSettings> notificationSettingsList = notificationSettingsRepository.findAll();
        assertThat(notificationSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationSettings testNotificationSettings = notificationSettingsList.get(notificationSettingsList.size() - 1);
        assertThat(testNotificationSettings.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testNotificationSettings.isAttendance()).isEqualTo(DEFAULT_ATTENDANCE);
        assertThat(testNotificationSettings.isHomework()).isEqualTo(DEFAULT_HOMEWORK);
        assertThat(testNotificationSettings.isNotice()).isEqualTo(DEFAULT_NOTICE);
    }

    @Test
    @Transactional
    public void createNotificationSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationSettingsRepository.findAll().size();

        // Create the NotificationSettings with an existing ID
        notificationSettings.setId(1L);
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationSettingsMockMvc.perform(post("/api/notification-settings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationSettings in the database
        List<NotificationSettings> notificationSettingsList = notificationSettingsRepository.findAll();
        assertThat(notificationSettingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotificationSettings() throws Exception {
        // Initialize the database
        notificationSettingsRepository.saveAndFlush(notificationSettings);

        // Get all the notificationSettingsList
        restNotificationSettingsMockMvc.perform(get("/api/notification-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].attendance").value(hasItem(DEFAULT_ATTENDANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].homework").value(hasItem(DEFAULT_HOMEWORK.booleanValue())))
            .andExpect(jsonPath("$.[*].notice").value(hasItem(DEFAULT_NOTICE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getNotificationSettings() throws Exception {
        // Initialize the database
        notificationSettingsRepository.saveAndFlush(notificationSettings);

        // Get the notificationSettings
        restNotificationSettingsMockMvc.perform(get("/api/notification-settings/{id}", notificationSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationSettings.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.attendance").value(DEFAULT_ATTENDANCE.booleanValue()))
            .andExpect(jsonPath("$.homework").value(DEFAULT_HOMEWORK.booleanValue()))
            .andExpect(jsonPath("$.notice").value(DEFAULT_NOTICE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationSettings() throws Exception {
        // Get the notificationSettings
        restNotificationSettingsMockMvc.perform(get("/api/notification-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationSettings() throws Exception {
        // Initialize the database
        notificationSettingsRepository.saveAndFlush(notificationSettings);

        int databaseSizeBeforeUpdate = notificationSettingsRepository.findAll().size();

        // Update the notificationSettings
        NotificationSettings updatedNotificationSettings = notificationSettingsRepository.findById(notificationSettings.getId()).get();
        // Disconnect from session so that the updates on updatedNotificationSettings are not directly saved in db
        em.detach(updatedNotificationSettings);
        updatedNotificationSettings
            .creationDate(UPDATED_CREATION_DATE)
            .attendance(UPDATED_ATTENDANCE)
            .homework(UPDATED_HOMEWORK)
            .notice(UPDATED_NOTICE);
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(updatedNotificationSettings);

        restNotificationSettingsMockMvc.perform(put("/api/notification-settings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationSettingsDTO)))
            .andExpect(status().isOk());

        // Validate the NotificationSettings in the database
        List<NotificationSettings> notificationSettingsList = notificationSettingsRepository.findAll();
        assertThat(notificationSettingsList).hasSize(databaseSizeBeforeUpdate);
        NotificationSettings testNotificationSettings = notificationSettingsList.get(notificationSettingsList.size() - 1);
        assertThat(testNotificationSettings.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testNotificationSettings.isAttendance()).isEqualTo(UPDATED_ATTENDANCE);
        assertThat(testNotificationSettings.isHomework()).isEqualTo(UPDATED_HOMEWORK);
        assertThat(testNotificationSettings.isNotice()).isEqualTo(UPDATED_NOTICE);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificationSettings() throws Exception {
        int databaseSizeBeforeUpdate = notificationSettingsRepository.findAll().size();

        // Create the NotificationSettings
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationSettingsMockMvc.perform(put("/api/notification-settings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationSettings in the database
        List<NotificationSettings> notificationSettingsList = notificationSettingsRepository.findAll();
        assertThat(notificationSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotificationSettings() throws Exception {
        // Initialize the database
        notificationSettingsRepository.saveAndFlush(notificationSettings);

        int databaseSizeBeforeDelete = notificationSettingsRepository.findAll().size();

        // Delete the notificationSettings
        restNotificationSettingsMockMvc.perform(delete("/api/notification-settings/{id}", notificationSettings.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NotificationSettings> notificationSettingsList = notificationSettingsRepository.findAll();
        assertThat(notificationSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
