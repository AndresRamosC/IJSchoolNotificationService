package com.ijrobotics.ijschoolnotificationservice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A NotificationSettings.
 */
@Entity
@Table(name = "notification_settings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NotificationSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "attendance")
    private Boolean attendance;

    @Column(name = "homework")
    private Boolean homework;

    @Column(name = "notice")
    private Boolean notice;

    @OneToOne
    @JoinColumn(unique = true)
    private Userid userid;

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

    public NotificationSettings creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isAttendance() {
        return attendance;
    }

    public NotificationSettings attendance(Boolean attendance) {
        this.attendance = attendance;
        return this;
    }

    public void setAttendance(Boolean attendance) {
        this.attendance = attendance;
    }

    public Boolean isHomework() {
        return homework;
    }

    public NotificationSettings homework(Boolean homework) {
        this.homework = homework;
        return this;
    }

    public void setHomework(Boolean homework) {
        this.homework = homework;
    }

    public Boolean isNotice() {
        return notice;
    }

    public NotificationSettings notice(Boolean notice) {
        this.notice = notice;
        return this;
    }

    public void setNotice(Boolean notice) {
        this.notice = notice;
    }

    public Userid getUserid() {
        return userid;
    }

    public NotificationSettings userid(Userid userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Userid userid) {
        this.userid = userid;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationSettings)) {
            return false;
        }
        return id != null && id.equals(((NotificationSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "NotificationSettings{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", attendance='" + isAttendance() + "'" +
            ", homework='" + isHomework() + "'" +
            ", notice='" + isNotice() + "'" +
            "}";
    }
}
