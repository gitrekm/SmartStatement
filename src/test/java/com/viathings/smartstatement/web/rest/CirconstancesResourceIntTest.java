package com.viathings.smartstatement.web.rest;

import com.viathings.smartstatement.SmartStatementApp;

import com.viathings.smartstatement.domain.Circonstances;
import com.viathings.smartstatement.repository.CirconstancesRepository;
import com.viathings.smartstatement.repository.search.CirconstancesSearchRepository;
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
 * Test class for the CirconstancesResource REST controller.
 *
 * @see CirconstancesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartStatementApp.class)
public class CirconstancesResourceIntTest {

    private static final String DEFAULT_CIRCONSTANCE = "AAAAAAAAAA";
    private static final String UPDATED_CIRCONSTANCE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CirconstancesRepository circonstancesRepository;

    @Autowired
    private CirconstancesSearchRepository circonstancesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCirconstancesMockMvc;

    private Circonstances circonstances;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CirconstancesResource circonstancesResource = new CirconstancesResource(circonstancesRepository, circonstancesSearchRepository);
        this.restCirconstancesMockMvc = MockMvcBuilders.standaloneSetup(circonstancesResource)
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
    public static Circonstances createEntity(EntityManager em) {
        Circonstances circonstances = new Circonstances()
            .circonstance(DEFAULT_CIRCONSTANCE)
            .description(DEFAULT_DESCRIPTION);
        return circonstances;
    }

    @Before
    public void initTest() {
        circonstancesSearchRepository.deleteAll();
        circonstances = createEntity(em);
    }

    @Test
    @Transactional
    public void createCirconstances() throws Exception {
        int databaseSizeBeforeCreate = circonstancesRepository.findAll().size();

        // Create the Circonstances
        restCirconstancesMockMvc.perform(post("/api/circonstances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circonstances)))
            .andExpect(status().isCreated());

        // Validate the Circonstances in the database
        List<Circonstances> circonstancesList = circonstancesRepository.findAll();
        assertThat(circonstancesList).hasSize(databaseSizeBeforeCreate + 1);
        Circonstances testCirconstances = circonstancesList.get(circonstancesList.size() - 1);
        assertThat(testCirconstances.getCirconstance()).isEqualTo(DEFAULT_CIRCONSTANCE);
        assertThat(testCirconstances.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Circonstances in Elasticsearch
        Circonstances circonstancesEs = circonstancesSearchRepository.findOne(testCirconstances.getId());
        assertThat(circonstancesEs).isEqualToComparingFieldByField(testCirconstances);
    }

    @Test
    @Transactional
    public void createCirconstancesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = circonstancesRepository.findAll().size();

        // Create the Circonstances with an existing ID
        circonstances.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCirconstancesMockMvc.perform(post("/api/circonstances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circonstances)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Circonstances> circonstancesList = circonstancesRepository.findAll();
        assertThat(circonstancesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCirconstances() throws Exception {
        // Initialize the database
        circonstancesRepository.saveAndFlush(circonstances);

        // Get all the circonstancesList
        restCirconstancesMockMvc.perform(get("/api/circonstances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circonstances.getId().intValue())))
            .andExpect(jsonPath("$.[*].circonstance").value(hasItem(DEFAULT_CIRCONSTANCE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCirconstances() throws Exception {
        // Initialize the database
        circonstancesRepository.saveAndFlush(circonstances);

        // Get the circonstances
        restCirconstancesMockMvc.perform(get("/api/circonstances/{id}", circonstances.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(circonstances.getId().intValue()))
            .andExpect(jsonPath("$.circonstance").value(DEFAULT_CIRCONSTANCE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCirconstances() throws Exception {
        // Get the circonstances
        restCirconstancesMockMvc.perform(get("/api/circonstances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCirconstances() throws Exception {
        // Initialize the database
        circonstancesRepository.saveAndFlush(circonstances);
        circonstancesSearchRepository.save(circonstances);
        int databaseSizeBeforeUpdate = circonstancesRepository.findAll().size();

        // Update the circonstances
        Circonstances updatedCirconstances = circonstancesRepository.findOne(circonstances.getId());
        updatedCirconstances
            .circonstance(UPDATED_CIRCONSTANCE)
            .description(UPDATED_DESCRIPTION);

        restCirconstancesMockMvc.perform(put("/api/circonstances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCirconstances)))
            .andExpect(status().isOk());

        // Validate the Circonstances in the database
        List<Circonstances> circonstancesList = circonstancesRepository.findAll();
        assertThat(circonstancesList).hasSize(databaseSizeBeforeUpdate);
        Circonstances testCirconstances = circonstancesList.get(circonstancesList.size() - 1);
        assertThat(testCirconstances.getCirconstance()).isEqualTo(UPDATED_CIRCONSTANCE);
        assertThat(testCirconstances.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Circonstances in Elasticsearch
        Circonstances circonstancesEs = circonstancesSearchRepository.findOne(testCirconstances.getId());
        assertThat(circonstancesEs).isEqualToComparingFieldByField(testCirconstances);
    }

    @Test
    @Transactional
    public void updateNonExistingCirconstances() throws Exception {
        int databaseSizeBeforeUpdate = circonstancesRepository.findAll().size();

        // Create the Circonstances

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCirconstancesMockMvc.perform(put("/api/circonstances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circonstances)))
            .andExpect(status().isCreated());

        // Validate the Circonstances in the database
        List<Circonstances> circonstancesList = circonstancesRepository.findAll();
        assertThat(circonstancesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCirconstances() throws Exception {
        // Initialize the database
        circonstancesRepository.saveAndFlush(circonstances);
        circonstancesSearchRepository.save(circonstances);
        int databaseSizeBeforeDelete = circonstancesRepository.findAll().size();

        // Get the circonstances
        restCirconstancesMockMvc.perform(delete("/api/circonstances/{id}", circonstances.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean circonstancesExistsInEs = circonstancesSearchRepository.exists(circonstances.getId());
        assertThat(circonstancesExistsInEs).isFalse();

        // Validate the database is empty
        List<Circonstances> circonstancesList = circonstancesRepository.findAll();
        assertThat(circonstancesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCirconstances() throws Exception {
        // Initialize the database
        circonstancesRepository.saveAndFlush(circonstances);
        circonstancesSearchRepository.save(circonstances);

        // Search the circonstances
        restCirconstancesMockMvc.perform(get("/api/_search/circonstances?query=id:" + circonstances.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circonstances.getId().intValue())))
            .andExpect(jsonPath("$.[*].circonstance").value(hasItem(DEFAULT_CIRCONSTANCE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Circonstances.class);
        Circonstances circonstances1 = new Circonstances();
        circonstances1.setId(1L);
        Circonstances circonstances2 = new Circonstances();
        circonstances2.setId(circonstances1.getId());
        assertThat(circonstances1).isEqualTo(circonstances2);
        circonstances2.setId(2L);
        assertThat(circonstances1).isNotEqualTo(circonstances2);
        circonstances1.setId(null);
        assertThat(circonstances1).isNotEqualTo(circonstances2);
    }
}
