package com.viathings.smartstatement.repository.search;

import com.viathings.smartstatement.domain.Vehicule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Vehicule entity.
 */
public interface VehiculeSearchRepository extends ElasticsearchRepository<Vehicule, Long> {
}
