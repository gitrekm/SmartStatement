package com.viathings.smartstatement.repository;

import com.viathings.smartstatement.domain.Constat;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Constat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConstatRepository extends JpaRepository<Constat,Long> {
    
    @Query("select distinct constat from Constat constat left join fetch constat.conducteurAS left join fetch constat.conducteurBS left join fetch constat.vehiculeAS left join fetch constat.vehiculeBS left join fetch constat.assuranceAS left join fetch constat.assuranceBS left join fetch constat.assureeAS left join fetch constat.assureeBS")
    List<Constat> findAllWithEagerRelationships();

    @Query("select constat from Constat constat left join fetch constat.conducteurAS left join fetch constat.conducteurBS left join fetch constat.vehiculeAS left join fetch constat.vehiculeBS left join fetch constat.assuranceAS left join fetch constat.assuranceBS left join fetch constat.assureeAS left join fetch constat.assureeBS where constat.id =:id")
    Constat findOneWithEagerRelationships(@Param("id") Long id);
    
}
