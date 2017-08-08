package com.viathings.smartstatement.repository.search;

import com.viathings.smartstatement.domain.Assurance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Assurance entity.
 */
public interface AssuranceSearchRepository extends ElasticsearchRepository<Assurance, Long> {
}
