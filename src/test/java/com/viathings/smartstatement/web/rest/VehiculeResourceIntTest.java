package com.viathings.smartstatement.web.rest;

import com.viathings.smartstatement.SmartStatementApp;

import com.viathings.smartstatement.domain.Vehicule;
import com.viathings.smartstatement.repository.VehiculeRepository;
import com.viathings.smartstatement.repository.search.VehiculeSearchRepository;
import com.viathings.smartstatement.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VehiculeResource REST controller.
 *
 * @see VehiculeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartStatementApp.class)
public class VehiculeResourceIntTest {

    private static final String DEFAULT_MARQUE = "AAAAAAAAAA";
    private static final String UPDATED_MARQUE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_IMMATRICULATION = "AAAAAAAAAA";
    private static final String UPDATED_IMMATRICULATION = "BBBBBBBBBB";

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private VehiculeSearchRepository vehiculeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVehiculeMockMvc;

    private Vehicule vehicule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VehiculeResource vehiculeResource = new VehiculeResource(vehiculeRepository, vehiculeSearchRepository);
        this.restVehiculeMockMvc = MockMvcBuilders.standaloneSetup(vehiculeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicule createEntity(EntityManager em) {
        Vehicule vehicule = new Vehicule()
            .marque(DEFAULT_MARQUE)
            .type(DEFAULT_TYPE)
            .immatriculation(DEFAULT_IMMATRICULATION);
        return vehicule;
    }

    @Before
    public void initTest() {
        vehiculeSearchRepository.deleteAll();
        vehicule = createEntity(em);
    }

    @Test
    @Transactional
    public void createVehicule() throws Exception {
        int databaseSizeBeforeCreate = vehiculeRepository.findAll().size();

        // Create the Vehicule
        restVehiculeMockMvc.perform(post("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicule)))
            .andExpect(status().isCreated());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeCreate + 1);
        Vehicule testVehicule = vehiculeList.get(vehiculeList.size() - 1);
        assertThat(testVehicule.getMarque()).isEqualTo(DEFAULT_MARQUE);
        assertThat(testVehicule.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVehicule.getImmatriculation()).isEqualTo(DEFAULT_IMMATRICULATION);

        // Validate the Vehicule in Elasticsearch
        Vehicule vehiculeEs = vehiculeSearchRepository.findOne(testVehicule.getId());
        assertThat(vehiculeEs).isEqualToComparingFieldByField(testVehicule);
    }

    @Test
    @Transactional
    public void createVehiculeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehiculeRepository.findAll().size();

        // Create the Vehicule with an existing ID
        vehicule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehiculeMockMvc.perform(post("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicule)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVehicules() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        // Get all the vehiculeList
        restVehiculeMockMvc.perform(get("/api/vehicules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicule.getId().intValue())))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].immatriculation").value(hasItem(DEFAULT_IMMATRICULATION.toString())));
    }

    @Test
    @Transactional
    public void getVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        // Get the vehicule
        restVehiculeMockMvc.perform(get("/api/vehicules/{id}", vehicule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vehicule.getId().intValue()))
            .andExpect(jsonPath("$.marque").value(DEFAULT_MARQUE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.immatriculation").value(DEFAULT_IMMATRICULATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVehicule() throws Exception {
        // Get the vehicule
        restVehiculeMockMvc.perform(get("/api/vehicules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);
        vehiculeSearchRepository.save(vehicule);
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();

        // Update the vehicule
        Vehicule updatedVehicule = vehiculeRepository.findOne(vehicule.getId());
        updatedVehicule
            .marque(UPDATED_MARQUE)
            .type(UPDATED_TYPE)
            .immatriculation(UPDATED_IMMATRICULATION);

        restVehiculeMockMvc.perform(put("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVehicule)))
            .andExpect(status().isOk());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
        Vehicule testVehicule = vehiculeList.get(vehiculeList.size() - 1);
        assertThat(testVehicule.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testVehicule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVehicule.getImmatriculation()).isEqualTo(UPDATED_IMMATRICULATION);

        // Validate the Vehicule in Elasticsearch
        Vehicule vehiculeEs = vehiculeSearchRepository.findOne(testVehicule.getId());
        assertThat(vehiculeEs).isEqualToComparingFieldByField(testVehicule);
    }

    @Test
    @Transactional
    public void updateNonExistingVehicule() throws Exception {
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();

        // Create the Vehicule

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVehiculeMockMvc.perform(put("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicule)))
            .andExpect(status().isCreated());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);
        vehiculeSearchRepository.save(vehicule);
        int databaseSizeBeforeDelete = vehiculeRepository.findAll().size();

        // Get the vehicule
        restVehiculeMockMvc.perform(delete("/api/vehicules/{id}", vehicule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean vehiculeExistsInEs = vehiculeSearchRepository.exists(vehicule.getId());
        assertThat(vehiculeExistsInEs).isFalse();

        // Validate the database is empty
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);
        vehiculeSearchRepository.save(vehicule);

        // Search the vehicule
        restVehiculeMockMvc.perform(get("/api/_search/vehicules?query=id:" + vehicule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicule.getId().intValue())))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].immatriculation").value(hasItem(DEFAULT_IMMATRICULATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicule.class);
        Vehicule vehicule1 = new Vehicule();
        vehicule1.setId(1L);
        Vehicule vehicule2 = new Vehicule();
        vehicule2.setId(vehicule1.getId());
        assertThat(vehicule1).isEqualTo(vehicule2);
        vehicule2.setId(2L);
        assertThat(vehicule1).isNotEqualTo(vehicule2);
        vehicule1.setId(null);
        assertThat(vehicule1).isNotEqualTo(vehicule2);
    }
}
