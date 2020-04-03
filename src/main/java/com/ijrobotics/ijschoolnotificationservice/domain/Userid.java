package com.ijrobotics.ijschoolnotificationservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Userid.
 */
@Entity
@Table(name = "userid")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Userid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "keycloak_user_id")
    private String keycloakUserId;

    @OneToMany(mappedBy = "keycloakUserId")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> notifications = new HashSet<>();

    @OneToOne(mappedBy = "userid")
    @JsonIgnore
    private NotificationSettings notificationSettings;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Userid creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getKeycloakUserId() {
        return keycloakUserId;
    }

    public Userid keycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
        return this;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Userid notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public Userid addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setKeycloakUserId(this);
        return this;
    }

    public Userid removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setKeycloakUserId(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }

    public Userid notificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
        return this;
    }

    public void setNotificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Userid)) {
            return false;
        }
        return id != null && id.equals(((Userid) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Userid{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", keycloakUserId='" + getKeycloakUserId() + "'" +
            "}";
    }
}
