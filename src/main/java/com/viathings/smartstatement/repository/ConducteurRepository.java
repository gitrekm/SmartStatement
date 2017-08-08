package com.viathings.smartstatement.repository;

import com.viathings.smartstatement.domain.Conducteur;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Conducteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConducteurRepository extends JpaRepository<Conducteur,Long> {
    
}
