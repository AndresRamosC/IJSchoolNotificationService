package com.ijrobotics.ijschoolnotificationservice.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ijrobotics.ijschoolnotificationservice.domain.Userid} entity.
 */
public class UseridDTO implements Serializable {

    private Long id;

    private ZonedDateTime creationDate;

    private String keycloakUserId;


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

    public String getKeycloakUserId() {
        return keycloakUserId;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UseridDTO useridDTO = (UseridDTO) o;
        if (useridDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), useridDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UseridDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", keycloakUserId='" + getKeycloakUserId() + "'" +
            "}";
    }
}
