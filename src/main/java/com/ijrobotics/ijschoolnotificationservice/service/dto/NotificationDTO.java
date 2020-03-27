package com.ijrobotics.ijschoolnotificationservice.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import com.ijrobotics.ijschoolnotificationservice.domain.enumeration.NotificationType;

/**
 * A DTO for the {@link com.ijrobotics.ijschoolnotificationservice.domain.Notification} entity.
 */
public class NotificationDTO implements Serializable {

    private Long id;

    private ZonedDateTime creationDate;

    private String title;

    private String description;

    private Boolean watched;

    private NotificationType notificationType;


    private Long keycloakUserIdId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isWatched() {
        return watched;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Long getKeycloakUserIdId() {
        return keycloakUserIdId;
    }

    public void setKeycloakUserIdId(Long useridId) {
        this.keycloakUserIdId = useridId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (notificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", watched='" + isWatched() + "'" +
            ", notificationType='" + getNotificationType() + "'" +
            ", keycloakUserIdId=" + getKeycloakUserIdId() +
            "}";
    }
}
