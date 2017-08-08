package com.viathings.smartstatement.repository.search;

import com.viathings.smartstatement.domain.Circonstances;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Circonstances entity.
 */
public interface CirconstancesSearchRepository extends ElasticsearchRepository<Circonstances, Long> {
}
