package com.ijrobotics.ijschoolnotificationservice.service;

import com.ijrobotics.ijschoolnotificationservice.domain.Userid;
import com.ijrobotics.ijschoolnotificationservice.repository.UseridRepository;
import com.ijrobotics.ijschoolnotificationservice.service.dto.UseridDTO;
import com.ijrobotics.ijschoolnotificationservice.service.mapper.UseridMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Userid}.
 */
@Service
@Transactional
public class UseridService {

    private final Logger log = LoggerFactory.getLogger(UseridService.class);

    private final UseridRepository useridRepository;

    private final UseridMapper useridMapper;

    public UseridService(UseridRepository useridRepository, UseridMapper useridMapper) {
        this.useridRepository = useridRepository;
        this.useridMapper = useridMapper;
    }

    /**
     * Save a userid.
     *
     * @param useridDTO the entity to save.
     * @return the persisted entity.
     */
    public UseridDTO save(UseridDTO useridDTO) {
        log.debug("Request to save Userid : {}", useridDTO);
        Userid userid = useridMapper.toEntity(useridDTO);
        userid = useridRepository.save(userid);
        return useridMapper.toDto(userid);
    }

    /**
     * Get all the userids.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UseridDTO> findAll() {
        log.debug("Request to get all Userids");
        return useridRepository.findAll().stream()
            .map(useridMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userid by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UseridDTO> findOne(Long id) {
        log.debug("Request to get Userid : {}", id);
        return useridRepository.findById(id)
            .map(useridMapper::toDto);
    }

    /**
     * Delete the userid by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Userid : {}", id);
        useridRepository.deleteById(id);
    }
    /**
     * Get one userid by keycloakUserId.
     *
     * @param keycloakUserId the keycloakUserId of the entity.
     * @return the entity.
     */
    public Optional<Userid> findByKeycloakUserId(String keycloakUserId) {
        log.debug("Request to get keycloakUserId : {}", keycloakUserId);
        return useridRepository.findByKeycloakUserId(keycloakUserId);
    }
}
