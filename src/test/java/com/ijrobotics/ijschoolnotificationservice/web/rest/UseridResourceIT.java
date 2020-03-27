package com.ijrobotics.ijschoolnotificationservice.web.rest;

import com.ijrobotics.ijschoolnotificationservice.IjSchoolManagerNotificationServiceApp;
import com.ijrobotics.ijschoolnotificationservice.config.TestSecurityConfiguration;
import com.ijrobotics.ijschoolnotificationservice.domain.Userid;
import com.ijrobotics.ijschoolnotificationservice.repository.UseridRepository;
import com.ijrobotics.ijschoolnotificationservice.service.UseridService;
import com.ijrobotics.ijschoolnotificationservice.service.dto.UseridDTO;
import com.ijrobotics.ijschoolnotificationservice.service.mapper.UseridMapper;
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
 * Integration tests for the {@link UseridResource} REST controller.
 */
@SpringBootTest(classes = {IjSchoolManagerNotificationServiceApp.class, TestSecurityConfiguration.class})
public class UseridResourceIT {

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_KEYCLOAK_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_KEYCLOAK_USER_ID = "BBBBBBBBBB";

    @Autowired
    private UseridRepository useridRepository;

    @Autowired
    private UseridMapper useridMapper;

    @Autowired
    private UseridService useridService;

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

    private MockMvc restUseridMockMvc;

    private Userid userid;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UseridResource useridResource = new UseridResource(useridService);
        this.restUseridMockMvc = MockMvcBuilders.standaloneSetup(useridResource)
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
    public static Userid createEntity(EntityManager em) {
        Userid userid = new Userid()
            .creationDate(DEFAULT_CREATION_DATE)
            .keycloakUserId(DEFAULT_KEYCLOAK_USER_ID);
        return userid;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Userid createUpdatedEntity(EntityManager em) {
        Userid userid = new Userid()
            .creationDate(UPDATED_CREATION_DATE)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID);
        return userid;
    }

    @BeforeEach
    public void initTest() {
        userid = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserid() throws Exception {
        int databaseSizeBeforeCreate = useridRepository.findAll().size();

        // Create the Userid
        UseridDTO useridDTO = useridMapper.toDto(userid);
        restUseridMockMvc.perform(post("/api/userids")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(useridDTO)))
            .andExpect(status().isCreated());

        // Validate the Userid in the database
        List<Userid> useridList = useridRepository.findAll();
        assertThat(useridList).hasSize(databaseSizeBeforeCreate + 1);
        Userid testUserid = useridList.get(useridList.size() - 1);
        assertThat(testUserid.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testUserid.getKeycloakUserId()).isEqualTo(DEFAULT_KEYCLOAK_USER_ID);
    }

    @Test
    @Transactional
    public void createUseridWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = useridRepository.findAll().size();

        // Create the Userid with an existing ID
        userid.setId(1L);
        UseridDTO useridDTO = useridMapper.toDto(userid);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUseridMockMvc.perform(post("/api/userids")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(useridDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Userid in the database
        List<Userid> useridList = useridRepository.findAll();
        assertThat(useridList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserids() throws Exception {
        // Initialize the database
        useridRepository.saveAndFlush(userid);

        // Get all the useridList
        restUseridMockMvc.perform(get("/api/userids?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userid.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].keycloakUserId").value(hasItem(DEFAULT_KEYCLOAK_USER_ID)));
    }
    
    @Test
    @Transactional
    public void getUserid() throws Exception {
        // Initialize the database
        useridRepository.saveAndFlush(userid);

        // Get the userid
        restUseridMockMvc.perform(get("/api/userids/{id}", userid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userid.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.keycloakUserId").value(DEFAULT_KEYCLOAK_USER_ID));
    }

    @Test
    @Transactional
    public void getNonExistingUserid() throws Exception {
        // Get the userid
        restUseridMockMvc.perform(get("/api/userids/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserid() throws Exception {
        // Initialize the database
        useridRepository.saveAndFlush(userid);

        int databaseSizeBeforeUpdate = useridRepository.findAll().size();

        // Update the userid
        Userid updatedUserid = useridRepository.findById(userid.getId()).get();
        // Disconnect from session so that the updates on updatedUserid are not directly saved in db
        em.detach(updatedUserid);
        updatedUserid
            .creationDate(UPDATED_CREATION_DATE)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID);
        UseridDTO useridDTO = useridMapper.toDto(updatedUserid);

        restUseridMockMvc.perform(put("/api/userids")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(useridDTO)))
            .andExpect(status().isOk());

        // Validate the Userid in the database
        List<Userid> useridList = useridRepository.findAll();
        assertThat(useridList).hasSize(databaseSizeBeforeUpdate);
        Userid testUserid = useridList.get(useridList.size() - 1);
        assertThat(testUserid.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testUserid.getKeycloakUserId()).isEqualTo(UPDATED_KEYCLOAK_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingUserid() throws Exception {
        int databaseSizeBeforeUpdate = useridRepository.findAll().size();

        // Create the Userid
        UseridDTO useridDTO = useridMapper.toDto(userid);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUseridMockMvc.perform(put("/api/userids")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(useridDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Userid in the database
        List<Userid> useridList = useridRepository.findAll();
        assertThat(useridList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserid() throws Exception {
        // Initialize the database
        useridRepository.saveAndFlush(userid);

        int databaseSizeBeforeDelete = useridRepository.findAll().size();

        // Delete the userid
        restUseridMockMvc.perform(delete("/api/userids/{id}", userid.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Userid> useridList = useridRepository.findAll();
        assertThat(useridList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
