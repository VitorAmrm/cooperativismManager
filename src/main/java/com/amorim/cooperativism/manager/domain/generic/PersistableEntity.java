package com.amorim.cooperativism.manager.domain.generic;

import jakarta.persistence.*;

@MappedSuperclass
public class PersistableEntity {

    private Long id;
    private Long version = 0L;
    private Boolean removed = false;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }
    @Column(name = "version")
    public void setVersion(Long version) {
        this.version = version;
    }
    @Column(name = "removed")
    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }
}
