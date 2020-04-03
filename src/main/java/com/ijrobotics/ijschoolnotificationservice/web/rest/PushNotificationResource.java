package com.ijrobotics.ijschoolnotificationservice.web.rest;

import com.ijrobotics.ijschoolnotificationservice.service.NotificationService;
import com.ijrobotics.ijschoolnotificationservice.service.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void sendPushNotification(@PathVariable List<String> keycloackUser, @RequestBody NotificationDTO notificationDTO) {
        notificationService.sendNotification(keycloackUser, notificationDTO);
    }

}
