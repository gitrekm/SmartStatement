package com.viathings.smartstatement.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.viathings.smartstatement.domain.Vehicule;

import com.viathings.smartstatement.repository.VehiculeRepository;
import com.viathings.smartstatement.repository.search.VehiculeSearchRepository;
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
 * REST controller for managing Vehicule.
 */
@RestController
@RequestMapping("/api")
public class VehiculeResource {

    private final Logger log = LoggerFactory.getLogger(VehiculeResource.class);

    private static final String ENTITY_NAME = "vehicule";

    private final VehiculeRepository vehiculeRepository;

    private final VehiculeSearchRepository vehiculeSearchRepository;

    public VehiculeResource(VehiculeRepository vehiculeRepository, VehiculeSearchRepository vehiculeSearchRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.vehiculeSearchRepository = vehiculeSearchRepository;
    }

    /**
     * POST  /vehicules : Create a new vehicule.
     *
     * @param vehicule the vehicule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vehicule, or with status 400 (Bad Request) if the vehicule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vehicules")
    @Timed
    public ResponseEntity<Vehicule> createVehicule(@RequestBody Vehicule vehicule) throws URISyntaxException {
        log.debug("REST request to save Vehicule : {}", vehicule);
        if (vehicule.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new vehicule cannot already have an ID")).body(null);
        }
        Vehicule result = vehiculeRepository.save(vehicule);
        vehiculeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vehicules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vehicules : Updates an existing vehicule.
     *
     * @param vehicule the vehicule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vehicule,
     * or with status 400 (Bad Request) if the vehicule is not valid,
     * or with status 500 (Internal Server Error) if the vehicule couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vehicules")
    @Timed
    public ResponseEntity<Vehicule> updateVehicule(@RequestBody Vehicule vehicule) throws URISyntaxException {
        log.debug("REST request to update Vehicule : {}", vehicule);
        if (vehicule.getId() == null) {
            return createVehicule(vehicule);
        }
        Vehicule result = vehiculeRepository.save(vehicule);
        vehiculeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vehicule.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vehicules : get all the vehicules.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of vehicules in body
     */
    @GetMapping("/vehicules")
    @Timed
    public List<Vehicule> getAllVehicules(@RequestParam(required = false) String filter) {
        if ("assuree-is-null".equals(filter)) {
            log.debug("REST request to get all Vehicules where assuree is null");
            return StreamSupport
                .stream(vehiculeRepository.findAll().spliterator(), false)
                .filter(vehicule -> vehicule.getAssuree() == null)
                .collect(Collectors.toList());
        }
        if ("conducteur-is-null".equals(filter)) {
            log.debug("REST request to get all Vehicules where conducteur is null");
            return StreamSupport
                .stream(vehiculeRepository.findAll().spliterator(), false)
                .filter(vehicule -> vehicule.getConducteur() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Vehicules");
        return vehiculeRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /vehicules/:id : get the "id" vehicule.
     *
     * @param id the id of the vehicule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vehicule, or with status 404 (Not Found)
     */
    @GetMapping("/vehicules/{id}")
    @Timed
    public ResponseEntity<Vehicule> getVehicule(@PathVariable Long id) {
        log.debug("REST request to get Vehicule : {}", id);
        Vehicule vehicule = vehiculeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vehicule));
    }

    /**
     * DELETE  /vehicules/:id : delete the "id" vehicule.
     *
     * @param id the id of the vehicule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vehicules/{id}")
    @Timed
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        log.debug("REST request to delete Vehicule : {}", id);
        vehiculeRepository.delete(id);
        vehiculeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/vehicules?query=:query : search for the vehicule corresponding
     * to the query.
     *
     * @param query the query of the vehicule search
     * @return the result of the search
     */
    @GetMapping("/_search/vehicules")
    @Timed
    public List<Vehicule> searchVehicules(@RequestParam String query) {
        log.debug("REST request to search Vehicules for query {}", query);
        return StreamSupport
            .stream(vehiculeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
