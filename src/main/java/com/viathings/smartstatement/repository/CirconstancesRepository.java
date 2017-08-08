package com.viathings.smartstatement.repository;

import com.viathings.smartstatement.domain.Circonstances;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Circonstances entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CirconstancesRepository extends JpaRepository<Circonstances,Long> {
    
}
