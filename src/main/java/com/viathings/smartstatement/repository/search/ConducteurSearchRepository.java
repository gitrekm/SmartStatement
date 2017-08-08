package com.viathings.smartstatement.repository.search;

import com.viathings.smartstatement.domain.Conducteur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Conducteur entity.
 */
public interface ConducteurSearchRepository extends ElasticsearchRepository<Conducteur, Long> {
}
