package com.viathings.smartstatement.repository.search;

import com.viathings.smartstatement.domain.Constat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Constat entity.
 */
public interface ConstatSearchRepository extends ElasticsearchRepository<Constat, Long> {
}
