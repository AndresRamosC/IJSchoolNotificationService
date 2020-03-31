package com.ijrobotics.ijschoolnotificationservice.web.rest;

import com.ijrobotics.ijschoolnotificationservice.domain.Userid;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/pushNotification")
public class PushNotificationResource {


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
        notificationService.sendNotification(keycloackUser,notificationDTO);
    }

}
