package com.viathings.smartstatement.web.rest;

import com.viathings.smartstatement.SmartStatementApp;

import com.viathings.smartstatement.domain.Assuree;
import com.viathings.smartstatement.repository.AssureeRepository;
import com.viathings.smartstatement.repository.search.AssureeSearchRepository;
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
 * Test class for the AssureeResource REST controller.
 *
 * @see AssureeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartStatementApp.class)
public class AssureeResourceIntTest {

    private static final String DEFAULT_NOM_ASSUREE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ASSUREE = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOMS_ASSUREE = "AAAAAAAAAA";
    private static final String UPDATED_PRENOMS_ASSUREE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_TEL = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TEL = "BBBBBBBBBB";

    @Autowired
    private AssureeRepository assureeRepository;

    @Autowired
    private AssureeSearchRepository assureeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAssureeMockMvc;

    private Assuree assuree;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssureeResource assureeResource = new AssureeResource(assureeRepository, assureeSearchRepository);
        this.restAssureeMockMvc = MockMvcBuilders.standaloneSetup(assureeResource)
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
    public static Assuree createEntity(EntityManager em) {
        Assuree assuree = new Assuree()
            .nomAssuree(DEFAULT_NOM_ASSUREE)
            .prenomsAssuree(DEFAULT_PRENOMS_ASSUREE)
            .addresse(DEFAULT_ADDRESSE)
            .numTel(DEFAULT_NUM_TEL);
        return assuree;
    }

    @Before
    public void initTest() {
        assureeSearchRepository.deleteAll();
        assuree = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssuree() throws Exception {
        int databaseSizeBeforeCreate = assureeRepository.findAll().size();

        // Create the Assuree
        restAssureeMockMvc.perform(post("/api/assurees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assuree)))
            .andExpect(status().isCreated());

        // Validate the Assuree in the database
        List<Assuree> assureeList = assureeRepository.findAll();
        assertThat(assureeList).hasSize(databaseSizeBeforeCreate + 1);
        Assuree testAssuree = assureeList.get(assureeList.size() - 1);
        assertThat(testAssuree.getNomAssuree()).isEqualTo(DEFAULT_NOM_ASSUREE);
        assertThat(testAssuree.getPrenomsAssuree()).isEqualTo(DEFAULT_PRENOMS_ASSUREE);
        assertThat(testAssuree.getAddresse()).isEqualTo(DEFAULT_ADDRESSE);
        assertThat(testAssuree.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);

        // Validate the Assuree in Elasticsearch
        Assuree assureeEs = assureeSearchRepository.findOne(testAssuree.getId());
        assertThat(assureeEs).isEqualToComparingFieldByField(testAssuree);
    }

    @Test
    @Transactional
    public void createAssureeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assureeRepository.findAll().size();

        // Create the Assuree with an existing ID
        assuree.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssureeMockMvc.perform(post("/api/assurees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assuree)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Assuree> assureeList = assureeRepository.findAll();
        assertThat(assureeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAssurees() throws Exception {
        // Initialize the database
        assureeRepository.saveAndFlush(assuree);

        // Get all the assureeList
        restAssureeMockMvc.perform(get("/api/assurees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assuree.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomAssuree").value(hasItem(DEFAULT_NOM_ASSUREE.toString())))
            .andExpect(jsonPath("$.[*].prenomsAssuree").value(hasItem(DEFAULT_PRENOMS_ASSUREE.toString())))
            .andExpect(jsonPath("$.[*].addresse").value(hasItem(DEFAULT_ADDRESSE.toString())))
            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL.toString())));
    }

    @Test
    @Transactional
    public void getAssuree() throws Exception {
        // Initialize the database
        assureeRepository.saveAndFlush(assuree);

        // Get the assuree
        restAssureeMockMvc.perform(get("/api/assurees/{id}", assuree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assuree.getId().intValue()))
            .andExpect(jsonPath("$.nomAssuree").value(DEFAULT_NOM_ASSUREE.toString()))
            .andExpect(jsonPath("$.prenomsAssuree").value(DEFAULT_PRENOMS_ASSUREE.toString()))
            .andExpect(jsonPath("$.addresse").value(DEFAULT_ADDRESSE.toString()))
            .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAssuree() throws Exception {
        // Get the assuree
        restAssureeMockMvc.perform(get("/api/assurees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssuree() throws Exception {
        // Initialize the database
        assureeRepository.saveAndFlush(assuree);
        assureeSearchRepository.save(assuree);
        int databaseSizeBeforeUpdate = assureeRepository.findAll().size();

        // Update the assuree
        Assuree updatedAssuree = assureeRepository.findOne(assuree.getId());
        updatedAssuree
            .nomAssuree(UPDATED_NOM_ASSUREE)
            .prenomsAssuree(UPDATED_PRENOMS_ASSUREE)
            .addresse(UPDATED_ADDRESSE)
            .numTel(UPDATED_NUM_TEL);

        restAssureeMockMvc.perform(put("/api/assurees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAssuree)))
            .andExpect(status().isOk());

        // Validate the Assuree in the database
        List<Assuree> assureeList = assureeRepository.findAll();
        assertThat(assureeList).hasSize(databaseSizeBeforeUpdate);
        Assuree testAssuree = assureeList.get(assureeList.size() - 1);
        assertThat(testAssuree.getNomAssuree()).isEqualTo(UPDATED_NOM_ASSUREE);
        assertThat(testAssuree.getPrenomsAssuree()).isEqualTo(UPDATED_PRENOMS_ASSUREE);
        assertThat(testAssuree.getAddresse()).isEqualTo(UPDATED_ADDRESSE);
        assertThat(testAssuree.getNumTel()).isEqualTo(UPDATED_NUM_TEL);

        // Validate the Assuree in Elasticsearch
        Assuree assureeEs = assureeSearchRepository.findOne(testAssuree.getId());
        assertThat(assureeEs).isEqualToComparingFieldByField(testAssuree);
    }

    @Test
    @Transactional
    public void updateNonExistingAssuree() throws Exception {
        int databaseSizeBeforeUpdate = assureeRepository.findAll().size();

        // Create the Assuree

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAssureeMockMvc.perform(put("/api/assurees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assuree)))
            .andExpect(status().isCreated());

        // Validate the Assuree in the database
        List<Assuree> assureeList = assureeRepository.findAll();
        assertThat(assureeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAssuree() throws Exception {
        // Initialize the database
        assureeRepository.saveAndFlush(assuree);
        assureeSearchRepository.save(assuree);
        int databaseSizeBeforeDelete = assureeRepository.findAll().size();

        // Get the assuree
        restAssureeMockMvc.perform(delete("/api/assurees/{id}", assuree.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean assureeExistsInEs = assureeSearchRepository.exists(assuree.getId());
        assertThat(assureeExistsInEs).isFalse();

        // Validate the database is empty
        List<Assuree> assureeList = assureeRepository.findAll();
        assertThat(assureeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAssuree() throws Exception {
        // Initialize the database
        assureeRepository.saveAndFlush(assuree);
        assureeSearchRepository.save(assuree);

        // Search the assuree
        restAssureeMockMvc.perform(get("/api/_search/assurees?query=id:" + assuree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assuree.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomAssuree").value(hasItem(DEFAULT_NOM_ASSUREE.toString())))
            .andExpect(jsonPath("$.[*].prenomsAssuree").value(hasItem(DEFAULT_PRENOMS_ASSUREE.toString())))
            .andExpect(jsonPath("$.[*].addresse").value(hasItem(DEFAULT_ADDRESSE.toString())))
            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assuree.class);
        Assuree assuree1 = new Assuree();
        assuree1.setId(1L);
        Assuree assuree2 = new Assuree();
        assuree2.setId(assuree1.getId());
        assertThat(assuree1).isEqualTo(assuree2);
        assuree2.setId(2L);
        assertThat(assuree1).isNotEqualTo(assuree2);
        assuree1.setId(null);
        assertThat(assuree1).isNotEqualTo(assuree2);
    }
}
