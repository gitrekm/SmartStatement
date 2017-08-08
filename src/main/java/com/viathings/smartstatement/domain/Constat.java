package com.viathings.smartstatement.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.viathings.smartstatement.domain.enumeration.YesNo;

/**
 * A Constat.
 */
@Entity
@Table(name = "constat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "constat")
public class Constat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_constat")
    private ZonedDateTime dateConstat;

    @Column(name = "lieu_constat")
    private String lieuConstat;

    @Column(name = "temoins")
    private String temoins;

    @Enumerated(EnumType.STRING)
    @Column(name = "degats")
    private YesNo degats;

    @Enumerated(EnumType.STRING)
    @Column(name = "degats_apparent")
    private YesNo degatsApparent;

    @Enumerated(EnumType.STRING)
    @Column(name = "blesses")
    private YesNo blesses;

    @Column(name = "point_de_choc_initial_a")
    private String pointDeChocInitialA;

    @Column(name = "observations_a")
    private String observationsA;

    @Column(name = "point_de_choc_initial_b")
    private String pointDeChocInitialB;

    @Column(name = "observations_b")
    private String observationsB;

    @Column(name = "sens_suivi_a")
    private String sensSuiviA;

    @Column(name = "venant_de_a")
    private String venantDeA;

    @Column(name = "allant_aa")
    private String allantAA;

    @Column(name = "sens_suivi_b")
    private String sensSuiviB;

    @Column(name = "venant_de_b")
    private String venantDeB;

    @Column(name = "allant_ab")
    private String allantAB;

    @OneToOne
    @JoinColumn(unique = true)
    private Circonstances circonstances;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "constat_conducteura",
               joinColumns = @JoinColumn(name="constats_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="conducteuras_id", referencedColumnName="id"))
    private Set<Conducteur> conducteurAS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "constat_conducteurb",
               joinColumns = @JoinColumn(name="constats_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="conducteurbs_id", referencedColumnName="id"))
    private Set<Conducteur> conducteurBS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "constat_vehiculea",
               joinColumns = @JoinColumn(name="constats_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="vehiculeas_id", referencedColumnName="id"))
    private Set<Vehicule> vehiculeAS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "constat_vehiculeb",
               joinColumns = @JoinColumn(name="constats_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="vehiculebs_id", referencedColumnName="id"))
    private Set<Vehicule> vehiculeBS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "constat_assurancea",
               joinColumns = @JoinColumn(name="constats_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="assuranceas_id", referencedColumnName="id"))
    private Set<Assurance> assuranceAS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "constat_assuranceb",
               joinColumns = @JoinColumn(name="constats_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="assurancebs_id", referencedColumnName="id"))
    private Set<Assurance> assuranceBS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "constat_assureea",
               joinColumns = @JoinColumn(name="constats_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="assureeas_id", referencedColumnName="id"))
    private Set<Assuree> assureeAS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "constat_assureeb",
               joinColumns = @JoinColumn(name="constats_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="assureebs_id", referencedColumnName="id"))
    private Set<Assuree> assureeBS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateConstat() {
        return dateConstat;
    }

    public Constat dateConstat(ZonedDateTime dateConstat) {
        this.dateConstat = dateConstat;
        return this;
    }

    public void setDateConstat(ZonedDateTime dateConstat) {
        this.dateConstat = dateConstat;
    }

    public String getLieuConstat() {
        return lieuConstat;
    }

    public Constat lieuConstat(String lieuConstat) {
        this.lieuConstat = lieuConstat;
        return this;
    }

    public void setLieuConstat(String lieuConstat) {
        this.lieuConstat = lieuConstat;
    }

    public String getTemoins() {
        return temoins;
    }

    public Constat temoins(String temoins) {
        this.temoins = temoins;
        return this;
    }

    public void setTemoins(String temoins) {
        this.temoins = temoins;
    }

    public YesNo getDegats() {
        return degats;
    }

    public Constat degats(YesNo degats) {
        this.degats = degats;
        return this;
    }

    public void setDegats(YesNo degats) {
        this.degats = degats;
    }

    public YesNo getDegatsApparent() {
        return degatsApparent;
    }

    public Constat degatsApparent(YesNo degatsApparent) {
        this.degatsApparent = degatsApparent;
        return this;
    }

    public void setDegatsApparent(YesNo degatsApparent) {
        this.degatsApparent = degatsApparent;
    }

    public YesNo getBlesses() {
        return blesses;
    }

    public Constat blesses(YesNo blesses) {
        this.blesses = blesses;
        return this;
    }

    public void setBlesses(YesNo blesses) {
        this.blesses = blesses;
    }

    public String getPointDeChocInitialA() {
        return pointDeChocInitialA;
    }

    public Constat pointDeChocInitialA(String pointDeChocInitialA) {
        this.pointDeChocInitialA = pointDeChocInitialA;
        return this;
    }

    public void setPointDeChocInitialA(String pointDeChocInitialA) {
        this.pointDeChocInitialA = pointDeChocInitialA;
    }

    public String getObservationsA() {
        return observationsA;
    }

    public Constat observationsA(String observationsA) {
        this.observationsA = observationsA;
        return this;
    }

    public void setObservationsA(String observationsA) {
        this.observationsA = observationsA;
    }

    public String getPointDeChocInitialB() {
        return pointDeChocInitialB;
    }

    public Constat pointDeChocInitialB(String pointDeChocInitialB) {
        this.pointDeChocInitialB = pointDeChocInitialB;
        return this;
    }

    public void setPointDeChocInitialB(String pointDeChocInitialB) {
        this.pointDeChocInitialB = pointDeChocInitialB;
    }

    public String getObservationsB() {
        return observationsB;
    }

    public Constat observationsB(String observationsB) {
        this.observationsB = observationsB;
        return this;
    }

    public void setObservationsB(String observationsB) {
        this.observationsB = observationsB;
    }

    public String getSensSuiviA() {
        return sensSuiviA;
    }

    public Constat sensSuiviA(String sensSuiviA) {
        this.sensSuiviA = sensSuiviA;
        return this;
    }

    public void setSensSuiviA(String sensSuiviA) {
        this.sensSuiviA = sensSuiviA;
    }

    public String getVenantDeA() {
        return venantDeA;
    }

    public Constat venantDeA(String venantDeA) {
        this.venantDeA = venantDeA;
        return this;
    }

    public void setVenantDeA(String venantDeA) {
        this.venantDeA = venantDeA;
    }

    public String getAllantAA() {
        return allantAA;
    }

    public Constat allantAA(String allantAA) {
        this.allantAA = allantAA;
        return this;
    }

    public void setAllantAA(String allantAA) {
        this.allantAA = allantAA;
    }

    public String getSensSuiviB() {
        return sensSuiviB;
    }

    public Constat sensSuiviB(String sensSuiviB) {
        this.sensSuiviB = sensSuiviB;
        return this;
    }

    public void setSensSuiviB(String sensSuiviB) {
        this.sensSuiviB = sensSuiviB;
    }

    public String getVenantDeB() {
        return venantDeB;
    }

    public Constat venantDeB(String venantDeB) {
        this.venantDeB = venantDeB;
        return this;
    }

    public void setVenantDeB(String venantDeB) {
        this.venantDeB = venantDeB;
    }

    public String getAllantAB() {
        return allantAB;
    }

    public Constat allantAB(String allantAB) {
        this.allantAB = allantAB;
        return this;
    }

    public void setAllantAB(String allantAB) {
        this.allantAB = allantAB;
    }

    public Circonstances getCirconstances() {
        return circonstances;
    }

    public Constat circonstances(Circonstances circonstances) {
        this.circonstances = circonstances;
        return this;
    }

    public void setCirconstances(Circonstances circonstances) {
        this.circonstances = circonstances;
    }

    public Set<Conducteur> getConducteurAS() {
        return conducteurAS;
    }

    public Constat conducteurAS(Set<Conducteur> conducteurs) {
        this.conducteurAS = conducteurs;
        return this;
    }

    public Constat addConducteurA(Conducteur conducteur) {
        this.conducteurAS.add(conducteur);
        conducteur.getConstatAS().add(this);
        return this;
    }

    public Constat removeConducteurA(Conducteur conducteur) {
        this.conducteurAS.remove(conducteur);
        conducteur.getConstatAS().remove(this);
        return this;
    }

    public void setConducteurAS(Set<Conducteur> conducteurs) {
        this.conducteurAS = conducteurs;
    }

    public Set<Conducteur> getConducteurBS() {
        return conducteurBS;
    }

    public Constat conducteurBS(Set<Conducteur> conducteurs) {
        this.conducteurBS = conducteurs;
        return this;
    }

    public Constat addConducteurB(Conducteur conducteur) {
        this.conducteurBS.add(conducteur);
        conducteur.getConstatBS().add(this);
        return this;
    }

    public Constat removeConducteurB(Conducteur conducteur) {
        this.conducteurBS.remove(conducteur);
        conducteur.getConstatBS().remove(this);
        return this;
    }

    public void setConducteurBS(Set<Conducteur> conducteurs) {
        this.conducteurBS = conducteurs;
    }

    public Set<Vehicule> getVehiculeAS() {
        return vehiculeAS;
    }

    public Constat vehiculeAS(Set<Vehicule> vehicules) {
        this.vehiculeAS = vehicules;
        return this;
    }

    public Constat addVehiculeA(Vehicule vehicule) {
        this.vehiculeAS.add(vehicule);
        vehicule.getConstatPartAS().add(this);
        return this;
    }

    public Constat removeVehiculeA(Vehicule vehicule) {
        this.vehiculeAS.remove(vehicule);
        vehicule.getConstatPartAS().remove(this);
        return this;
    }

    public void setVehiculeAS(Set<Vehicule> vehicules) {
        this.vehiculeAS = vehicules;
    }

    public Set<Vehicule> getVehiculeBS() {
        return vehiculeBS;
    }

    public Constat vehiculeBS(Set<Vehicule> vehicules) {
        this.vehiculeBS = vehicules;
        return this;
    }

    public Constat addVehiculeB(Vehicule vehicule) {
        this.vehiculeBS.add(vehicule);
        vehicule.getConstatPartBS().add(this);
        return this;
    }

    public Constat removeVehiculeB(Vehicule vehicule) {
        this.vehiculeBS.remove(vehicule);
        vehicule.getConstatPartBS().remove(this);
        return this;
    }

    public void setVehiculeBS(Set<Vehicule> vehicules) {
        this.vehiculeBS = vehicules;
    }

    public Set<Assurance> getAssuranceAS() {
        return assuranceAS;
    }

    public Constat assuranceAS(Set<Assurance> assurances) {
        this.assuranceAS = assurances;
        return this;
    }

    public Constat addAssuranceA(Assurance assurance) {
        this.assuranceAS.add(assurance);
        assurance.getConstatPartAS().add(this);
        return this;
    }

    public Constat removeAssuranceA(Assurance assurance) {
        this.assuranceAS.remove(assurance);
        assurance.getConstatPartAS().remove(this);
        return this;
    }

    public void setAssuranceAS(Set<Assurance> assurances) {
        this.assuranceAS = assurances;
    }

    public Set<Assurance> getAssuranceBS() {
        return assuranceBS;
    }

    public Constat assuranceBS(Set<Assurance> assurances) {
        this.assuranceBS = assurances;
        return this;
    }

    public Constat addAssuranceB(Assurance assurance) {
        this.assuranceBS.add(assurance);
        assurance.getConstatPartBS().add(this);
        return this;
    }

    public Constat removeAssuranceB(Assurance assurance) {
        this.assuranceBS.remove(assurance);
        assurance.getConstatPartBS().remove(this);
        return this;
    }

    public void setAssuranceBS(Set<Assurance> assurances) {
        this.assuranceBS = assurances;
    }

    public Set<Assuree> getAssureeAS() {
        return assureeAS;
    }

    public Constat assureeAS(Set<Assuree> assurees) {
        this.assureeAS = assurees;
        return this;
    }

    public Constat addAssureeA(Assuree assuree) {
        this.assureeAS.add(assuree);
        assuree.getConstatPartAS().add(this);
        return this;
    }

    public Constat removeAssureeA(Assuree assuree) {
        this.assureeAS.remove(assuree);
        assuree.getConstatPartAS().remove(this);
        return this;
    }

    public void setAssureeAS(Set<Assuree> assurees) {
        this.assureeAS = assurees;
    }

    public Set<Assuree> getAssureeBS() {
        return assureeBS;
    }

    public Constat assureeBS(Set<Assuree> assurees) {
        this.assureeBS = assurees;
        return this;
    }

    public Constat addAssureeB(Assuree assuree) {
        this.assureeBS.add(assuree);
        assuree.getConstatPartBS().add(this);
        return this;
    }

    public Constat removeAssureeB(Assuree assuree) {
        this.assureeBS.remove(assuree);
        assuree.getConstatPartBS().remove(this);
        return this;
    }

    public void setAssureeBS(Set<Assuree> assurees) {
        this.assureeBS = assurees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Constat constat = (Constat) o;
        if (constat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), constat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Constat{" +
            "id=" + getId() +
            ", dateConstat='" + getDateConstat() + "'" +
            ", lieuConstat='" + getLieuConstat() + "'" +
            ", temoins='" + getTemoins() + "'" +
            ", degats='" + getDegats() + "'" +
            ", degatsApparent='" + getDegatsApparent() + "'" +
            ", blesses='" + getBlesses() + "'" +
            ", pointDeChocInitialA='" + getPointDeChocInitialA() + "'" +
            ", observationsA='" + getObservationsA() + "'" +
            ", pointDeChocInitialB='" + getPointDeChocInitialB() + "'" +
            ", observationsB='" + getObservationsB() + "'" +
            ", sensSuiviA='" + getSensSuiviA() + "'" +
            ", venantDeA='" + getVenantDeA() + "'" +
            ", allantAA='" + getAllantAA() + "'" +
            ", sensSuiviB='" + getSensSuiviB() + "'" +
            ", venantDeB='" + getVenantDeB() + "'" +
            ", allantAB='" + getAllantAB() + "'" +
            "}";
    }
}
