package com.ijrobotics.ijschoolnotificationservice.web.rest;

import com.ijrobotics.ijschoolnotificationservice.service.UseridService;
import com.ijrobotics.ijschoolnotificationservice.web.rest.errors.BadRequestAlertException;
import com.ijrobotics.ijschoolnotificationservice.service.dto.UseridDTO;

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
 * REST controller for managing {@link com.ijrobotics.ijschoolnotificationservice.domain.Userid}.
 */
@RestController
@RequestMapping("/api")
public class UseridResource {

    private final Logger log = LoggerFactory.getLogger(UseridResource.class);

    private static final String ENTITY_NAME = "ijSchoolManagerNotificationServiceUserid";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UseridService useridService;

    public UseridResource(UseridService useridService) {
        this.useridService = useridService;
    }

    /**
     * {@code POST  /userids} : Create a new userid.
     *
     * @param useridDTO the useridDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new useridDTO, or with status {@code 400 (Bad Request)} if the userid has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/userids")
    public ResponseEntity<UseridDTO> createUserid(@RequestBody UseridDTO useridDTO) throws URISyntaxException {
        log.debug("REST request to save Userid : {}", useridDTO);
        if (useridDTO.getId() != null) {
            throw new BadRequestAlertException("A new userid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UseridDTO result = useridService.save(useridDTO);
        return ResponseEntity.created(new URI("/api/userids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /userids} : Updates an existing userid.
     *
     * @param useridDTO the useridDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated useridDTO,
     * or with status {@code 400 (Bad Request)} if the useridDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the useridDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/userids")
    public ResponseEntity<UseridDTO> updateUserid(@RequestBody UseridDTO useridDTO) throws URISyntaxException {
        log.debug("REST request to update Userid : {}", useridDTO);
        if (useridDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UseridDTO result = useridService.save(useridDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, useridDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /userids} : get all the userids.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userids in body.
     */
    @GetMapping("/userids")
    public List<UseridDTO> getAllUserids() {
        log.debug("REST request to get all Userids");
        return useridService.findAll();
    }

    /**
     * {@code GET  /userids/:id} : get the "id" userid.
     *
     * @param id the id of the useridDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the useridDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/userids/{id}")
    public ResponseEntity<UseridDTO> getUserid(@PathVariable Long id) {
        log.debug("REST request to get Userid : {}", id);
        Optional<UseridDTO> useridDTO = useridService.findOne(id);
        return ResponseUtil.wrapOrNotFound(useridDTO);
    }

    /**
     * {@code DELETE  /userids/:id} : delete the "id" userid.
     *
     * @param id the id of the useridDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/userids/{id}")
    public ResponseEntity<Void> deleteUserid(@PathVariable Long id) {
        log.debug("REST request to delete Userid : {}", id);
        useridService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
