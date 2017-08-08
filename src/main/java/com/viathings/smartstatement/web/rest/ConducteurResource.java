package com.viathings.smartstatement.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.viathings.smartstatement.domain.Conducteur;

import com.viathings.smartstatement.repository.ConducteurRepository;
import com.viathings.smartstatement.repository.search.ConducteurSearchRepository;
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
 * REST controller for managing Conducteur.
 */
@RestController
@RequestMapping("/api")
public class ConducteurResource {

    private final Logger log = LoggerFactory.getLogger(ConducteurResource.class);

    private static final String ENTITY_NAME = "conducteur";

    private final ConducteurRepository conducteurRepository;

    private final ConducteurSearchRepository conducteurSearchRepository;

    public ConducteurResource(ConducteurRepository conducteurRepository, ConducteurSearchRepository conducteurSearchRepository) {
        this.conducteurRepository = conducteurRepository;
        this.conducteurSearchRepository = conducteurSearchRepository;
    }

    /**
     * POST  /conducteurs : Create a new conducteur.
     *
     * @param conducteur the conducteur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conducteur, or with status 400 (Bad Request) if the conducteur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/conducteurs")
    @Timed
    public ResponseEntity<Conducteur> createConducteur(@RequestBody Conducteur conducteur) throws URISyntaxException {
        log.debug("REST request to save Conducteur : {}", conducteur);
        if (conducteur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new conducteur cannot already have an ID")).body(null);
        }
        Conducteur result = conducteurRepository.save(conducteur);
        conducteurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/conducteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conducteurs : Updates an existing conducteur.
     *
     * @param conducteur the conducteur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conducteur,
     * or with status 400 (Bad Request) if the conducteur is not valid,
     * or with status 500 (Internal Server Error) if the conducteur couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/conducteurs")
    @Timed
    public ResponseEntity<Conducteur> updateConducteur(@RequestBody Conducteur conducteur) throws URISyntaxException {
        log.debug("REST request to update Conducteur : {}", conducteur);
        if (conducteur.getId() == null) {
            return createConducteur(conducteur);
        }
        Conducteur result = conducteurRepository.save(conducteur);
        conducteurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, conducteur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conducteurs : get all the conducteurs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of conducteurs in body
     */
    @GetMapping("/conducteurs")
    @Timed
    public List<Conducteur> getAllConducteurs() {
        log.debug("REST request to get all Conducteurs");
        return conducteurRepository.findAll();
    }

    /**
     * GET  /conducteurs/:id : get the "id" conducteur.
     *
     * @param id the id of the conducteur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conducteur, or with status 404 (Not Found)
     */
    @GetMapping("/conducteurs/{id}")
    @Timed
    public ResponseEntity<Conducteur> getConducteur(@PathVariable Long id) {
        log.debug("REST request to get Conducteur : {}", id);
        Conducteur conducteur = conducteurRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(conducteur));
    }

    /**
     * DELETE  /conducteurs/:id : delete the "id" conducteur.
     *
     * @param id the id of the conducteur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/conducteurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteConducteur(@PathVariable Long id) {
        log.debug("REST request to delete Conducteur : {}", id);
        conducteurRepository.delete(id);
        conducteurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/conducteurs?query=:query : search for the conducteur corresponding
     * to the query.
     *
     * @param query the query of the conducteur search
     * @return the result of the search
     */
    @GetMapping("/_search/conducteurs")
    @Timed
    public List<Conducteur> searchConducteurs(@RequestParam String query) {
        log.debug("REST request to search Conducteurs for query {}", query);
        return StreamSupport
            .stream(conducteurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
