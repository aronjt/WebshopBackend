package org.progmatic.webshop.model;

import org.progmatic.webshop.helpers.DateFormatHelper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue
    private long id;

    @DateTimeFormat(pattern = DateFormatHelper.DATE_TIME_FORMAT)
    private LocalDateTime creationTime;

    @PrePersist
    public void init() {
        creationTime = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
