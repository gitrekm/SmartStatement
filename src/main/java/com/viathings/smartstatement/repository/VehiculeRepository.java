package com.viathings.smartstatement.repository;

import com.viathings.smartstatement.domain.Vehicule;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Vehicule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule,Long> {
    
    @Query("select distinct vehicule from Vehicule vehicule left join fetch vehicule.noms")
    List<Vehicule> findAllWithEagerRelationships();

    @Query("select vehicule from Vehicule vehicule left join fetch vehicule.noms where vehicule.id =:id")
    Vehicule findOneWithEagerRelationships(@Param("id") Long id);
    
}
