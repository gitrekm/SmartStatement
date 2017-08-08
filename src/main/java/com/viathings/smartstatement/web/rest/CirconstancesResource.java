package com.viathings.smartstatement.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.viathings.smartstatement.domain.Circonstances;

import com.viathings.smartstatement.repository.CirconstancesRepository;
import com.viathings.smartstatement.repository.search.CirconstancesSearchRepository;
import com.viathings.smartstatement.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Circonstances.
 */
@RestController
@RequestMapping("/api")
public class CirconstancesResource {

    private final Logger log = LoggerFactory.getLogger(CirconstancesResource.class);

    private static final String ENTITY_NAME = "circonstances";

    private final CirconstancesRepository circonstancesRepository;

    private final CirconstancesSearchRepository circonstancesSearchRepository;

    public CirconstancesResource(CirconstancesRepository circonstancesRepository, CirconstancesSearchRepository circonstancesSearchRepository) {
        this.circonstancesRepository = circonstancesRepository;
        this.circonstancesSearchRepository = circonstancesSearchRepository;
    }

    /**
     * POST  /circonstances : Create a new circonstances.
     *
     * @param circonstances the circonstances to create
     * @return the ResponseEntity with status 201 (Created) and with body the new circonstances, or with status 400 (Bad Request) if the circonstances has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/circonstances")
    @Timed
    public ResponseEntity<Circonstances> createCirconstances(@RequestBody Circonstances circonstances) throws URISyntaxException {
        log.debug("REST request to save Circonstances : {}", circonstances);
        if (circonstances.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new circonstances cannot already have an ID")).body(null);
        }
        Circonstances result = circonstancesRepository.save(circonstances);
        circonstancesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/circonstances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /circonstances : Updates an existing circonstances.
     *
     * @param circonstances the circonstances to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated circonstances,
     * or with status 400 (Bad Request) if the circonstances is not valid,
     * or with status 500 (Internal Server Error) if the circonstances couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/circonstances")
    @Timed
    public ResponseEntity<Circonstances> updateCirconstances(@RequestBody Circonstances circonstances) throws URISyntaxException {
        log.debug("REST request to update Circonstances : {}", circonstances);
        if (circonstances.getId() == null) {
            return createCirconstances(circonstances);
        }
        Circonstances result = circonstancesRepository.save(circonstances);
        circonstancesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, circonstances.getId().toString()))
            .body(result);
    }

    /**
     * GET  /circonstances : get all the circonstances.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of circonstances in body
     */
    @GetMapping("/circonstances")
    @Timed
    public List<Circonstances> getAllCirconstances(@RequestParam(required = false) String filter) {
        if ("constat-is-null".equals(filter)) {
            log.debug("REST request to get all Circonstancess where constat is null");
            return StreamSupport
                .stream(circonstancesRepository.findAll().spliterator(), false)
                .filter(circonstances -> circonstances.getConstat() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Circonstances");
        return circonstancesRepository.findAll();
    }

    /**
     * GET  /circonstances/:id : get the "id" circonstances.
     *
     * @param id the id of the circonstances to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the circonstances, or with status 404 (Not Found)
     */
    @GetMapping("/circonstances/{id}")
    @Timed
    public ResponseEntity<Circonstances> getCirconstances(@PathVariable Long id) {
        log.debug("REST request to get Circonstances : {}", id);
        Circonstances circonstances = circonstancesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(circonstances));
    }

    /**
     * DELETE  /circonstances/:id : delete the "id" circonstances.
     *
     * @param id the id of the circonstances to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/circonstances/{id}")
    @Timed
    public ResponseEntity<Void> deleteCirconstances(@PathVariable Long id) {
        log.debug("REST request to delete Circonstances : {}", id);
        circonstancesRepository.delete(id);
        circonstancesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/circonstances?query=:query : search for the circonstances corresponding
     * to the query.
     *
     * @param query the query of the circonstances search
     * @return the result of the search
     */
    @GetMapping("/_search/circonstances")
    @Timed
    public List<Circonstances> searchCirconstances(@RequestParam String query) {
        log.debug("REST request to search Circonstances for query {}", query);
        return StreamSupport
            .stream(circonstancesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
