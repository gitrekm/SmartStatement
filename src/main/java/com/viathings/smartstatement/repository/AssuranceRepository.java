package com.viathings.smartstatement.repository;

import com.viathings.smartstatement.domain.Assurance;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Assurance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssuranceRepository extends JpaRepository<Assurance,Long> {
    
}
