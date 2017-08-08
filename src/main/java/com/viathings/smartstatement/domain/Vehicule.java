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
 * A Vehicule.
 */
@Entity
@Table(name = "vehicule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "vehicule")
public class Vehicule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marque")
    private String marque;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "immatriculation")
    private String immatriculation;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "vehicule_nom",
               joinColumns = @JoinColumn(name="vehicules_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="noms_id", referencedColumnName="id"))
    private Set<Conducteur> noms = new HashSet<>();

    @OneToOne(mappedBy = "vehicule")
    @JsonIgnore
    private Assuree assuree;

    @OneToOne(mappedBy = "vehicleConducteur")
    @JsonIgnore
    private Assuree conducteur;

    @ManyToMany(mappedBy = "vehiculeAS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Constat> constatPartAS = new HashSet<>();

    @ManyToMany(mappedBy = "vehiculeBS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Constat> constatPartBS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public Vehicule marque(String marque) {
        this.marque = marque;
        return this;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getType() {
        return type;
    }

    public Vehicule type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public Vehicule immatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
        return this;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public Set<Conducteur> getNoms() {
        return noms;
    }

    public Vehicule noms(Set<Conducteur> conducteurs) {
        this.noms = conducteurs;
        return this;
    }

    public Vehicule addNom(Conducteur conducteur) {
        this.noms.add(conducteur);
        conducteur.getVehicules().add(this);
        return this;
    }

    public Vehicule removeNom(Conducteur conducteur) {
        this.noms.remove(conducteur);
        conducteur.getVehicules().remove(this);
        return this;
    }

    public void setNoms(Set<Conducteur> conducteurs) {
        this.noms = conducteurs;
    }

    public Assuree getAssuree() {
        return assuree;
    }

    public Vehicule assuree(Assuree assuree) {
        this.assuree = assuree;
        return this;
    }

    public void setAssuree(Assuree assuree) {
        this.assuree = assuree;
    }

    public Assuree getConducteur() {
        return conducteur;
    }

    public Vehicule conducteur(Assuree assuree) {
        this.conducteur = assuree;
        return this;
    }

    public void setConducteur(Assuree assuree) {
        this.conducteur = assuree;
    }

    public Set<Constat> getConstatPartAS() {
        return constatPartAS;
    }

    public Vehicule constatPartAS(Set<Constat> constats) {
        this.constatPartAS = constats;
        return this;
    }

    public Vehicule addConstatPartA(Constat constat) {
        this.constatPartAS.add(constat);
        constat.getVehiculeAS().add(this);
        return this;
    }

    public Vehicule removeConstatPartA(Constat constat) {
        this.constatPartAS.remove(constat);
        constat.getVehiculeAS().remove(this);
        return this;
    }

    public void setConstatPartAS(Set<Constat> constats) {
        this.constatPartAS = constats;
    }

    public Set<Constat> getConstatPartBS() {
        return constatPartBS;
    }

    public Vehicule constatPartBS(Set<Constat> constats) {
        this.constatPartBS = constats;
        return this;
    }

    public Vehicule addConstatPartB(Constat constat) {
        this.constatPartBS.add(constat);
        constat.getVehiculeBS().add(this);
        return this;
    }

    public Vehicule removeConstatPartB(Constat constat) {
        this.constatPartBS.remove(constat);
        constat.getVehiculeBS().remove(this);
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
        Vehicule vehicule = (Vehicule) o;
        if (vehicule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehicule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vehicule{" +
            "id=" + getId() +
            ", marque='" + getMarque() + "'" +
            ", type='" + getType() + "'" +
            ", immatriculation='" + getImmatriculation() + "'" +
            "}";
    }
}
