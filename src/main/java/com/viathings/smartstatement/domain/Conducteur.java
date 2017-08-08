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
 * A Conducteur.
 */
@Entity
@Table(name = "conducteur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "conducteur")
public class Conducteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email")
    private String email;

    @Column(name = "tel")
    private String tel;

    @Column(name = "permis_num")
    private Long permisNum;

    @Column(name = "livree_le")
    private ZonedDateTime livreeLe;

    @Column(name = "addresse")
    private String addresse;

    @ManyToMany(mappedBy = "conducteurAS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Constat> constatAS = new HashSet<>();

    @ManyToMany(mappedBy = "conducteurBS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Constat> constatBS = new HashSet<>();

    @ManyToMany(mappedBy = "noms")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vehicule> vehicules = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Conducteur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Conducteur prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public Conducteur email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public Conducteur tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Long getPermisNum() {
        return permisNum;
    }

    public Conducteur permisNum(Long permisNum) {
        this.permisNum = permisNum;
        return this;
    }

    public void setPermisNum(Long permisNum) {
        this.permisNum = permisNum;
    }

    public ZonedDateTime getLivreeLe() {
        return livreeLe;
    }

    public Conducteur livreeLe(ZonedDateTime livreeLe) {
        this.livreeLe = livreeLe;
        return this;
    }

    public void setLivreeLe(ZonedDateTime livreeLe) {
        this.livreeLe = livreeLe;
    }

    public String getAddresse() {
        return addresse;
    }

    public Conducteur addresse(String addresse) {
        this.addresse = addresse;
        return this;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public Set<Constat> getConstatAS() {
        return constatAS;
    }

    public Conducteur constatAS(Set<Constat> constats) {
        this.constatAS = constats;
        return this;
    }

    public Conducteur addConstatA(Constat constat) {
        this.constatAS.add(constat);
        constat.getConducteurAS().add(this);
        return this;
    }

    public Conducteur removeConstatA(Constat constat) {
        this.constatAS.remove(constat);
        constat.getConducteurAS().remove(this);
        return this;
    }

    public void setConstatAS(Set<Constat> constats) {
        this.constatAS = constats;
    }

    public Set<Constat> getConstatBS() {
        return constatBS;
    }

    public Conducteur constatBS(Set<Constat> constats) {
        this.constatBS = constats;
        return this;
    }

    public Conducteur addConstatB(Constat constat) {
        this.constatBS.add(constat);
        constat.getConducteurBS().add(this);
        return this;
    }

    public Conducteur removeConstatB(Constat constat) {
        this.constatBS.remove(constat);
        constat.getConducteurBS().remove(this);
        return this;
    }

    public void setConstatBS(Set<Constat> constats) {
        this.constatBS = constats;
    }

    public Set<Vehicule> getVehicules() {
        return vehicules;
    }

    public Conducteur vehicules(Set<Vehicule> vehicules) {
        this.vehicules = vehicules;
        return this;
    }

    public Conducteur addVehicule(Vehicule vehicule) {
        this.vehicules.add(vehicule);
        vehicule.getNoms().add(this);
        return this;
    }

    public Conducteur removeVehicule(Vehicule vehicule) {
        this.vehicules.remove(vehicule);
        vehicule.getNoms().remove(this);
        return this;
    }

    public void setVehicules(Set<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conducteur conducteur = (Conducteur) o;
        if (conducteur.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conducteur.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Conducteur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", tel='" + getTel() + "'" +
            ", permisNum='" + getPermisNum() + "'" +
            ", livreeLe='" + getLivreeLe() + "'" +
            ", addresse='" + getAddresse() + "'" +
            "}";
    }
}
