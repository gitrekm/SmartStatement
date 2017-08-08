package com.viathings.smartstatement.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.viathings.smartstatement.domain.Assurance;

import com.viathings.smartstatement.repository.AssuranceRepository;
import com.viathings.smartstatement.repository.search.AssuranceSearchRepository;
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
 * REST controller for managing Assurance.
 */
@RestController
@RequestMapping("/api")
public class AssuranceResource {

    private final Logger log = LoggerFactory.getLogger(AssuranceResource.class);

    private static final String ENTITY_NAME = "assurance";

    private final AssuranceRepository assuranceRepository;

    private final AssuranceSearchRepository assuranceSearchRepository;

    public AssuranceResource(AssuranceRepository assuranceRepository, AssuranceSearchRepository assuranceSearchRepository) {
        this.assuranceRepository = assuranceRepository;
        this.assuranceSearchRepository = assuranceSearchRepository;
    }

    /**
     * POST  /assurances : Create a new assurance.
     *
     * @param assurance the assurance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assurance, or with status 400 (Bad Request) if the assurance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/assurances")
    @Timed
    public ResponseEntity<Assurance> createAssurance(@RequestBody Assurance assurance) throws URISyntaxException {
        log.debug("REST request to save Assurance : {}", assurance);
        if (assurance.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new assurance cannot already have an ID")).body(null);
        }
        Assurance result = assuranceRepository.save(assurance);
        assuranceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/assurances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assurances : Updates an existing assurance.
     *
     * @param assurance the assurance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assurance,
     * or with status 400 (Bad Request) if the assurance is not valid,
     * or with status 500 (Internal Server Error) if the assurance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/assurances")
    @Timed
    public ResponseEntity<Assurance> updateAssurance(@RequestBody Assurance assurance) throws URISyntaxException {
        log.debug("REST request to update Assurance : {}", assurance);
        if (assurance.getId() == null) {
            return createAssurance(assurance);
        }
        Assurance result = assuranceRepository.save(assurance);
        assuranceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, assurance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assurances : get all the assurances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of assurances in body
     */
    @GetMapping("/assurances")
    @Timed
    public List<Assurance> getAllAssurances() {
        log.debug("REST request to get all Assurances");
        return assuranceRepository.findAll();
    }

    /**
     * GET  /assurances/:id : get the "id" assurance.
     *
     * @param id the id of the assurance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assurance, or with status 404 (Not Found)
     */
    @GetMapping("/assurances/{id}")
    @Timed
    public ResponseEntity<Assurance> getAssurance(@PathVariable Long id) {
        log.debug("REST request to get Assurance : {}", id);
        Assurance assurance = assuranceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(assurance));
    }

    /**
     * DELETE  /assurances/:id : delete the "id" assurance.
     *
     * @param id the id of the assurance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/assurances/{id}")
    @Timed
    public ResponseEntity<Void> deleteAssurance(@PathVariable Long id) {
        log.debug("REST request to delete Assurance : {}", id);
        assuranceRepository.delete(id);
        assuranceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/assurances?query=:query : search for the assurance corresponding
     * to the query.
     *
     * @param query the query of the assurance search
     * @return the result of the search
     */
    @GetMapping("/_search/assurances")
    @Timed
    public List<Assurance> searchAssurances(@RequestParam String query) {
        log.debug("REST request to search Assurances for query {}", query);
        return StreamSupport
            .stream(assuranceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
