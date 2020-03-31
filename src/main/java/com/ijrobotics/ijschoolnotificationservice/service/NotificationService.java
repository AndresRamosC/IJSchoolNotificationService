package com.ijrobotics.ijschoolnotificationservice.service;

import com.ijrobotics.ijschoolnotificationservice.domain.Notification;
import com.ijrobotics.ijschoolnotificationservice.domain.Userid;
import com.ijrobotics.ijschoolnotificationservice.repository.NotificationRepository;
import com.ijrobotics.ijschoolnotificationservice.service.dto.NotificationDTO;
import com.ijrobotics.ijschoolnotificationservice.service.dto.UseridDTO;
import com.ijrobotics.ijschoolnotificationservice.service.mapper.NotificationMapper;
import com.ijrobotics.ijschoolnotificationservice.web.rest.PushNotificationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;
    private Client client = ClientBuilder.newClient();
    private WebTarget baseURl=client.target("https://onesignal.com/api/v1");
    private String auth="Basic NTk2ZjA2ODAtMWM0MS00ZWQ5LThmNTAtZWNmYWZiZGE5MmJj";

    private
    UseridService useridService;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper,UseridService useridService) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.useridService=useridService;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }
    /**
     * Send a notification.
     *
     * @param notificationDTO the entity to send.
     * @return the persisted entity.
     */
    public NotificationDTO sendNotification(String keycloackUser,NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Optional<Userid> useridDTO= useridService.findByKeycloakUserId(keycloackUser);
        notificationDTO.setCreationDate(ZonedDateTime.now());
        if (useridDTO.isPresent()){
            //User already on Database
            log.info("USER FOUNDED: "+ useridDTO.get().toString());
            notificationDTO.setKeycloakUserIdId(useridDTO.get().getId());
            System.out.println(useridDTO.get().toString());
        }else{
            log.info("USER NOT FOUNDED: ");
            //Add user to the database
            UseridDTO saveUser= new UseridDTO();
            saveUser.setCreationDate(ZonedDateTime.now());
            saveUser.setKeycloakUserId(keycloackUser);
            notificationDTO.setKeycloakUserIdId(useridService.save(saveUser).getId());
        }

        String contents = "{ " +
            "\"app_id\"            : \"a46c4d47-1635-4f77-9166-a579b77b9fff\", " +
            "\"contents\"            : {\"en\" : \""+ notificationDTO.getDescription()+"\" }, " +
            "\"included_segments\" : [ \"All\" ] " +
            "}";
        Response response=baseReq("/notifications").post(Entity.json(contents));
        System.out.println("response = " + response.getStatus());
        System.out.println("response = " + response.readEntity(String.class));
        return this.save(notificationDTO);
    }

    /**
     * Get all the notifications.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> findAll() {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll().stream()
            .map(notificationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id)
            .map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }
    private Invocation.Builder baseReq(String path){
        return baseURl.path(path).request(MediaType.APPLICATION_JSON)
            .header("Content-Type","application/json; charset=UTF-8")
            .header("Authorization", this.auth);
    }
}
