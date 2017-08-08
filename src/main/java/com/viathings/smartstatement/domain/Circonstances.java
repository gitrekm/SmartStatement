package com.viathings.smartstatement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Circonstances.
 */
@Entity
@Table(name = "circonstances")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "circonstances")
public class Circonstances implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "circonstance")
    private String circonstance;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "circonstances")
    @JsonIgnore
    private Constat constat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCirconstance() {
        return circonstance;
    }

    public Circonstances circonstance(String circonstance) {
        this.circonstance = circonstance;
        return this;
    }

    public void setCirconstance(String circonstance) {
        this.circonstance = circonstance;
    }

    public String getDescription() {
        return description;
    }

    public Circonstances description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Constat getConstat() {
        return constat;
    }

    public Circonstances constat(Constat constat) {
        this.constat = constat;
        return this;
    }

    public void setConstat(Constat constat) {
        this.constat = constat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Circonstances circonstances = (Circonstances) o;
        if (circonstances.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), circonstances.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Circonstances{" +
            "id=" + getId() +
            ", circonstance='" + getCirconstance() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
