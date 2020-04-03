package com.ijrobotics.ijschoolnotificationservice.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ijrobotics.ijschoolnotificationservice.domain.NotificationSettings} entity.
 */
public class NotificationSettingsDTO implements Serializable {

    private Long id;

    private ZonedDateTime creationDate;

    private Boolean attendance;

    private Boolean homework;

    private Boolean notice;


    private Long useridId;

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

    public Boolean isAttendance() {
        return attendance;
    }

    public void setAttendance(Boolean attendance) {
        this.attendance = attendance;
    }

    public Boolean isHomework() {
        return homework;
    }

    public void setHomework(Boolean homework) {
        this.homework = homework;
    }

    public Boolean isNotice() {
        return notice;
    }

    public void setNotice(Boolean notice) {
        this.notice = notice;
    }

    public Long getUseridId() {
        return useridId;
    }

    public void setUseridId(Long useridId) {
        this.useridId = useridId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationSettingsDTO notificationSettingsDTO = (NotificationSettingsDTO) o;
        if (notificationSettingsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationSettingsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationSettingsDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", attendance='" + isAttendance() + "'" +
            ", homework='" + isHomework() + "'" +
            ", notice='" + isNotice() + "'" +
            ", useridId=" + getUseridId() +
            "}";
    }
}
