package com.viathings.smartstatement.web.rest;

import com.viathings.smartstatement.SmartStatementApp;

import com.viathings.smartstatement.domain.Assurance;
import com.viathings.smartstatement.repository.AssuranceRepository;
import com.viathings.smartstatement.repository.search.AssuranceSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.viathings.smartstatement.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AssuranceResource REST controller.
 *
 * @see AssuranceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartStatementApp.class)
public class AssuranceResourceIntTest {

    private static final String DEFAULT_NOM_ASSURANCE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ASSURANCE = "BBBBBBBBBB";

    private static final Long DEFAULT_NUM_ASSURNANCE = 1L;
    private static final Long UPDATED_NUM_ASSURNANCE = 2L;

    private static final String DEFAULT_AGENCE = "AAAAAAAAAA";
    private static final String UPDATED_AGENCE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_VALABLE_AU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALABLE_AU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AssuranceRepository assuranceRepository;

    @Autowired
    private AssuranceSearchRepository assuranceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAssuranceMockMvc;

    private Assurance assurance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssuranceResource assuranceResource = new AssuranceResource(assuranceRepository, assuranceSearchRepository);
        this.restAssuranceMockMvc = MockMvcBuilders.standaloneSetup(assuranceResource)
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
    public static Assurance createEntity(EntityManager em) {
        Assurance assurance = new Assurance()
            .nomAssurance(DEFAULT_NOM_ASSURANCE)
            .numAssurnance(DEFAULT_NUM_ASSURNANCE)
            .agence(DEFAULT_AGENCE)
            .valableAu(DEFAULT_VALABLE_AU);
        return assurance;
    }

    @Before
    public void initTest() {
        assuranceSearchRepository.deleteAll();
        assurance = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssurance() throws Exception {
        int databaseSizeBeforeCreate = assuranceRepository.findAll().size();

        // Create the Assurance
        restAssuranceMockMvc.perform(post("/api/assurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assurance)))
            .andExpect(status().isCreated());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeCreate + 1);
        Assurance testAssurance = assuranceList.get(assuranceList.size() - 1);
        assertThat(testAssurance.getNomAssurance()).isEqualTo(DEFAULT_NOM_ASSURANCE);
        assertThat(testAssurance.getNumAssurnance()).isEqualTo(DEFAULT_NUM_ASSURNANCE);
        assertThat(testAssurance.getAgence()).isEqualTo(DEFAULT_AGENCE);
        assertThat(testAssurance.getValableAu()).isEqualTo(DEFAULT_VALABLE_AU);

        // Validate the Assurance in Elasticsearch
        Assurance assuranceEs = assuranceSearchRepository.findOne(testAssurance.getId());
        assertThat(assuranceEs).isEqualToComparingFieldByField(testAssurance);
    }

    @Test
    @Transactional
    public void createAssuranceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assuranceRepository.findAll().size();

        // Create the Assurance with an existing ID
        assurance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssuranceMockMvc.perform(post("/api/assurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assurance)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAssurances() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);

        // Get all the assuranceList
        restAssuranceMockMvc.perform(get("/api/assurances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assurance.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomAssurance").value(hasItem(DEFAULT_NOM_ASSURANCE.toString())))
            .andExpect(jsonPath("$.[*].numAssurnance").value(hasItem(DEFAULT_NUM_ASSURNANCE.intValue())))
            .andExpect(jsonPath("$.[*].agence").value(hasItem(DEFAULT_AGENCE.toString())))
            .andExpect(jsonPath("$.[*].valableAu").value(hasItem(sameInstant(DEFAULT_VALABLE_AU))));
    }

    @Test
    @Transactional
    public void getAssurance() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);

        // Get the assurance
        restAssuranceMockMvc.perform(get("/api/assurances/{id}", assurance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assurance.getId().intValue()))
            .andExpect(jsonPath("$.nomAssurance").value(DEFAULT_NOM_ASSURANCE.toString()))
            .andExpect(jsonPath("$.numAssurnance").value(DEFAULT_NUM_ASSURNANCE.intValue()))
            .andExpect(jsonPath("$.agence").value(DEFAULT_AGENCE.toString()))
            .andExpect(jsonPath("$.valableAu").value(sameInstant(DEFAULT_VALABLE_AU)));
    }

    @Test
    @Transactional
    public void getNonExistingAssurance() throws Exception {
        // Get the assurance
        restAssuranceMockMvc.perform(get("/api/assurances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssurance() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);
        assuranceSearchRepository.save(assurance);
        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();

        // Update the assurance
        Assurance updatedAssurance = assuranceRepository.findOne(assurance.getId());
        updatedAssurance
            .nomAssurance(UPDATED_NOM_ASSURANCE)
            .numAssurnance(UPDATED_NUM_ASSURNANCE)
            .agence(UPDATED_AGENCE)
            .valableAu(UPDATED_VALABLE_AU);

        restAssuranceMockMvc.perform(put("/api/assurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAssurance)))
            .andExpect(status().isOk());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
        Assurance testAssurance = assuranceList.get(assuranceList.size() - 1);
        assertThat(testAssurance.getNomAssurance()).isEqualTo(UPDATED_NOM_ASSURANCE);
        assertThat(testAssurance.getNumAssurnance()).isEqualTo(UPDATED_NUM_ASSURNANCE);
        assertThat(testAssurance.getAgence()).isEqualTo(UPDATED_AGENCE);
        assertThat(testAssurance.getValableAu()).isEqualTo(UPDATED_VALABLE_AU);

        // Validate the Assurance in Elasticsearch
        Assurance assuranceEs = assuranceSearchRepository.findOne(testAssurance.getId());
        assertThat(assuranceEs).isEqualToComparingFieldByField(testAssurance);
    }

    @Test
    @Transactional
    public void updateNonExistingAssurance() throws Exception {
        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();

        // Create the Assurance

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAssuranceMockMvc.perform(put("/api/assurances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assurance)))
            .andExpect(status().isCreated());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAssurance() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);
        assuranceSearchRepository.save(assurance);
        int databaseSizeBeforeDelete = assuranceRepository.findAll().size();

        // Get the assurance
        restAssuranceMockMvc.perform(delete("/api/assurances/{id}", assurance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean assuranceExistsInEs = assuranceSearchRepository.exists(assurance.getId());
        assertThat(assuranceExistsInEs).isFalse();

        // Validate the database is empty
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAssurance() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);
        assuranceSearchRepository.save(assurance);

        // Search the assurance
        restAssuranceMockMvc.perform(get("/api/_search/assurances?query=id:" + assurance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assurance.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomAssurance").value(hasItem(DEFAULT_NOM_ASSURANCE.toString())))
            .andExpect(jsonPath("$.[*].numAssurnance").value(hasItem(DEFAULT_NUM_ASSURNANCE.intValue())))
            .andExpect(jsonPath("$.[*].agence").value(hasItem(DEFAULT_AGENCE.toString())))
            .andExpect(jsonPath("$.[*].valableAu").value(hasItem(sameInstant(DEFAULT_VALABLE_AU))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assurance.class);
        Assurance assurance1 = new Assurance();
        assurance1.setId(1L);
        Assurance assurance2 = new Assurance();
        assurance2.setId(assurance1.getId());
        assertThat(assurance1).isEqualTo(assurance2);
        assurance2.setId(2L);
        assertThat(assurance1).isNotEqualTo(assurance2);
        assurance1.setId(null);
        assertThat(assurance1).isNotEqualTo(assurance2);
    }
}
