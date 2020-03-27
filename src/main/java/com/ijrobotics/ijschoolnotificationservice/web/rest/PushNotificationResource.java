package com.ijrobotics.ijschoolnotificationservice.web.rest;

import com.ijrobotics.ijschoolnotificationservice.domain.Notification;
import com.ijrobotics.ijschoolnotificationservice.domain.Userid;
import com.ijrobotics.ijschoolnotificationservice.domain.enumeration.NotificationType;
import com.ijrobotics.ijschoolnotificationservice.service.NotificationService;
import com.ijrobotics.ijschoolnotificationservice.service.UseridService;
import com.ijrobotics.ijschoolnotificationservice.service.dto.NotificationDTO;
import com.ijrobotics.ijschoolnotificationservice.service.dto.UseridDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/api/pushNotification")
public class PushNotificationResource {
    private final Logger log = LoggerFactory.getLogger(PushNotificationResource.class);
    private static final String ENTITY_NAME = "ijSchoolNotificationServicePushNotificationResource";
    private Client client = ClientBuilder.newClient();
    private WebTarget baseURl=client.target("https://onesignal.com/api/v1");
    private String auth="Basic NTk2ZjA2ODAtMWM0MS00ZWQ5LThmNTAtZWNmYWZiZGE5MmJj";

    @Autowired
    private
    UseridService useridService;
    @Autowired
    private
    NotificationService notificationService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    public PushNotificationResource() {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

    @PostMapping("/Notice/{keycloackUser}")
    @ResponseStatus(HttpStatus.OK)
    public void sendPushNotification(@PathVariable String keycloackUser,@RequestBody NotificationDTO notificationDTO) {
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
        notificationService.save(notificationDTO);

//        String contents = "{ " +
//            "\"app_id\"            : \"a46c4d47-1635-4f77-9166-a579b77b9fff\", " +
//            "\"contents\"            : {\"en\" : \"Java test\"}, " +
//            "\"included_segments\" : [ \"All\" ] " +
//            "}";
//        Response response=baseReq("/notifications").post(Entity.json(contents));
//        System.out.println("response = " + response.getStatus());
//        System.out.println("response = " + response.readEntity(String.class));
//        System.out.println("send notice");
    }

    private Invocation.Builder baseReq(String path){
        return baseURl.path(path).request(MediaType.APPLICATION_JSON)
            .header("Content-Type","application/json; charset=UTF-8")
            .header("Authorization", this.auth);
    }
}
