package com.jbpmtask.application.data.entity;

import java.util.UUID;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import dev.hilla.Nonnull;
import org.hibernate.annotations.Type;

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
