package com.viathings.smartstatement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Assuree.
 */
@Entity
@Table(name = "assuree")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "assuree")
public class Assuree implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_assuree")
    private String nomAssuree;

    @Column(name = "prenoms_assuree")
    private String prenomsAssuree;

    @Column(name = "addresse")
    private String addresse;

    @Column(name = "num_tel")
    private String numTel;

    @OneToOne
    @JoinColumn(unique = true)
    private Vehicule vehicule;

    @OneToOne
    @JoinColumn(unique = true)
    private Vehicule vehicleConducteur;

    @ManyToMany(mappedBy = "assureeAS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Constat> constatPartAS = new HashSet<>();

    @ManyToMany(mappedBy = "assureeBS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Constat> constatPartBS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomAssuree() {
        return nomAssuree;
    }

    public Assuree nomAssuree(String nomAssuree) {
        this.nomAssuree = nomAssuree;
        return this;
    }

    public void setNomAssuree(String nomAssuree) {
        this.nomAssuree = nomAssuree;
    }

    public String getPrenomsAssuree() {
        return prenomsAssuree;
    }

    public Assuree prenomsAssuree(String prenomsAssuree) {
        this.prenomsAssuree = prenomsAssuree;
        return this;
    }

    public void setPrenomsAssuree(String prenomsAssuree) {
        this.prenomsAssuree = prenomsAssuree;
    }

    public String getAddresse() {
        return addresse;
    }

    public Assuree addresse(String addresse) {
        this.addresse = addresse;
        return this;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getNumTel() {
        return numTel;
    }

    public Assuree numTel(String numTel) {
        this.numTel = numTel;
        return this;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public Assuree vehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
        return this;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Vehicule getVehicleConducteur() {
        return vehicleConducteur;
    }

    public Assuree vehicleConducteur(Vehicule vehicule) {
        this.vehicleConducteur = vehicule;
        return this;
    }

    public void setVehicleConducteur(Vehicule vehicule) {
        this.vehicleConducteur = vehicule;
    }

    public Set<Constat> getConstatPartAS() {
        return constatPartAS;
    }

    public Assuree constatPartAS(Set<Constat> constats) {
        this.constatPartAS = constats;
        return this;
    }

    public Assuree addConstatPartA(Constat constat) {
        this.constatPartAS.add(constat);
        constat.getAssureeAS().add(this);
        return this;
    }

    public Assuree removeConstatPartA(Constat constat) {
        this.constatPartAS.remove(constat);
        constat.getAssureeAS().remove(this);
        return this;
    }

    public void setConstatPartAS(Set<Constat> constats) {
        this.constatPartAS = constats;
    }

    public Set<Constat> getConstatPartBS() {
        return constatPartBS;
    }

    public Assuree constatPartBS(Set<Constat> constats) {
        this.constatPartBS = constats;
        return this;
    }

    public Assuree addConstatPartB(Constat constat) {
        this.constatPartBS.add(constat);
        constat.getAssureeBS().add(this);
        return this;
    }

    public Assuree removeConstatPartB(Constat constat) {
        this.constatPartBS.remove(constat);
        constat.getAssureeBS().remove(this);
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
        Assuree assuree = (Assuree) o;
        if (assuree.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assuree.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Assuree{" +
            "id=" + getId() +
            ", nomAssuree='" + getNomAssuree() + "'" +
            ", prenomsAssuree='" + getPrenomsAssuree() + "'" +
            ", addresse='" + getAddresse() + "'" +
            ", numTel='" + getNumTel() + "'" +
            "}";
    }
}
