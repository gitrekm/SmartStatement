package com.viathings.smartstatement.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.viathings.smartstatement.domain.Constat;

import com.viathings.smartstatement.repository.ConstatRepository;
import com.viathings.smartstatement.repository.search.ConstatSearchRepository;
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
 * REST controller for managing Constat.
 */
@RestController
@RequestMapping("/api")
public class ConstatResource {

    private final Logger log = LoggerFactory.getLogger(ConstatResource.class);

    private static final String ENTITY_NAME = "constat";

    private final ConstatRepository constatRepository;

    private final ConstatSearchRepository constatSearchRepository;

    public ConstatResource(ConstatRepository constatRepository, ConstatSearchRepository constatSearchRepository) {
        this.constatRepository = constatRepository;
        this.constatSearchRepository = constatSearchRepository;
    }

    /**
     * POST  /constats : Create a new constat.
     *
     * @param constat the constat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new constat, or with status 400 (Bad Request) if the constat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/constats")
    @Timed
    public ResponseEntity<Constat> createConstat(@RequestBody Constat constat) throws URISyntaxException {
        log.debug("REST request to save Constat : {}", constat);
        if (constat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new constat cannot already have an ID")).body(null);
        }
        Constat result = constatRepository.save(constat);
        constatSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/constats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /constats : Updates an existing constat.
     *
     * @param constat the constat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated constat,
     * or with status 400 (Bad Request) if the constat is not valid,
     * or with status 500 (Internal Server Error) if the constat couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/constats")
    @Timed
    public ResponseEntity<Constat> updateConstat(@RequestBody Constat constat) throws URISyntaxException {
        log.debug("REST request to update Constat : {}", constat);
        if (constat.getId() == null) {
            return createConstat(constat);
        }
        Constat result = constatRepository.save(constat);
        constatSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, constat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /constats : get all the constats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of constats in body
     */
    @GetMapping("/constats")
    @Timed
    public List<Constat> getAllConstats() {
        log.debug("REST request to get all Constats");
        return constatRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /constats/:id : get the "id" constat.
     *
     * @param id the id of the constat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the constat, or with status 404 (Not Found)
     */
    @GetMapping("/constats/{id}")
    @Timed
    public ResponseEntity<Constat> getConstat(@PathVariable Long id) {
        log.debug("REST request to get Constat : {}", id);
        Constat constat = constatRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(constat));
    }

    /**
     * DELETE  /constats/:id : delete the "id" constat.
     *
     * @param id the id of the constat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/constats/{id}")
    @Timed
    public ResponseEntity<Void> deleteConstat(@PathVariable Long id) {
        log.debug("REST request to delete Constat : {}", id);
        constatRepository.delete(id);
        constatSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/constats?query=:query : search for the constat corresponding
     * to the query.
     *
     * @param query the query of the constat search
     * @return the result of the search
     */
    @GetMapping("/_search/constats")
    @Timed
    public List<Constat> searchConstats(@RequestParam String query) {
        log.debug("REST request to search Constats for query {}", query);
        return StreamSupport
            .stream(constatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
