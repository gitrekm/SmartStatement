package com.viathings.smartstatement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Assurance.
 */
@Entity
@Table(name = "assurance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "assurance")
public class Assurance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_assurance")
    private String nomAssurance;

    @Column(name = "num_assurnance")
    private Long numAssurnance;

    @Column(name = "agence")
    private String agence;

    @Column(name = "valable_au")
    private ZonedDateTime valableAu;

    @ManyToMany(mappedBy = "assuranceAS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Constat> constatPartAS = new HashSet<>();

    @ManyToMany(mappedBy = "assuranceBS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Constat> constatPartBS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomAssurance() {
        return nomAssurance;
    }

    public Assurance nomAssurance(String nomAssurance) {
        this.nomAssurance = nomAssurance;
        return this;
    }

    public void setNomAssurance(String nomAssurance) {
        this.nomAssurance = nomAssurance;
    }

    public Long getNumAssurnance() {
        return numAssurnance;
    }

    public Assurance numAssurnance(Long numAssurnance) {
        this.numAssurnance = numAssurnance;
        return this;
    }

    public void setNumAssurnance(Long numAssurnance) {
        this.numAssurnance = numAssurnance;
    }

    public String getAgence() {
        return agence;
    }

    public Assurance agence(String agence) {
        this.agence = agence;
        return this;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public ZonedDateTime getValableAu() {
        return valableAu;
    }

    public Assurance valableAu(ZonedDateTime valableAu) {
        this.valableAu = valableAu;
        return this;
    }

    public void setValableAu(ZonedDateTime valableAu) {
        this.valableAu = valableAu;
    }

    public Set<Constat> getConstatPartAS() {
        return constatPartAS;
    }

    public Assurance constatPartAS(Set<Constat> constats) {
        this.constatPartAS = constats;
        return this;
    }

    public Assurance addConstatPartA(Constat constat) {
        this.constatPartAS.add(constat);
        constat.getAssuranceAS().add(this);
        return this;
    }

    public Assurance removeConstatPartA(Constat constat) {
        this.constatPartAS.remove(constat);
        constat.getAssuranceAS().remove(this);
        return this;
    }

    public void setConstatPartAS(Set<Constat> constats) {
        this.constatPartAS = constats;
    }

    public Set<Constat> getConstatPartBS() {
        return constatPartBS;
    }

    public Assurance constatPartBS(Set<Constat> constats) {
        this.constatPartBS = constats;
        return this;
    }

    public Assurance addConstatPartB(Constat constat) {
        this.constatPartBS.add(constat);
        constat.getAssuranceBS().add(this);
        return this;
    }

    public Assurance removeConstatPartB(Constat constat) {
        this.constatPartBS.remove(constat);
        constat.getAssuranceBS().remove(this);
        return this;
    }

    public void setConstatPartBS(Set<Constat> constats) {
        this.constatPartBS = constats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Assurance assurance = (Assurance) o;
        if (assurance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assurance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Assurance{" +
            "id=" + getId() +
            ", nomAssurance='" + getNomAssurance() + "'" +
            ", numAssurnance='" + getNumAssurnance() + "'" +
            ", agence='" + getAgence() + "'" +
            ", valableAu='" + getValableAu() + "'" +
            "}";
    }
}
