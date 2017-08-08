package com.viathings.smartstatement.web.rest;

import com.viathings.smartstatement.SmartStatementApp;

import com.viathings.smartstatement.domain.Conducteur;
import com.viathings.smartstatement.repository.ConducteurRepository;
import com.viathings.smartstatement.repository.search.ConducteurSearchRepository;
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
 * Test class for the ConducteurResource REST controller.
 *
 * @see ConducteurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartStatementApp.class)
public class ConducteurResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final Long DEFAULT_PERMIS_NUM = 1L;
    private static final Long UPDATED_PERMIS_NUM = 2L;

    private static final ZonedDateTime DEFAULT_LIVREE_LE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LIVREE_LE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_ADDRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESSE = "BBBBBBBBBB";

    @Autowired
    private ConducteurRepository conducteurRepository;

    @Autowired
    private ConducteurSearchRepository conducteurSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConducteurMockMvc;

    private Conducteur conducteur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConducteurResource conducteurResource = new ConducteurResource(conducteurRepository, conducteurSearchRepository);
        this.restConducteurMockMvc = MockMvcBuilders.standaloneSetup(conducteurResource)
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
    public static Conducteur createEntity(EntityManager em) {
        Conducteur conducteur = new Conducteur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .tel(DEFAULT_TEL)
            .permisNum(DEFAULT_PERMIS_NUM)
            .livreeLe(DEFAULT_LIVREE_LE)
            .addresse(DEFAULT_ADDRESSE);
        return conducteur;
    }

    @Before
    public void initTest() {
        conducteurSearchRepository.deleteAll();
        conducteur = createEntity(em);
    }

    @Test
    @Transactional
    public void createConducteur() throws Exception {
        int databaseSizeBeforeCreate = conducteurRepository.findAll().size();

        // Create the Conducteur
        restConducteurMockMvc.perform(post("/api/conducteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conducteur)))
            .andExpect(status().isCreated());

        // Validate the Conducteur in the database
        List<Conducteur> conducteurList = conducteurRepository.findAll();
        assertThat(conducteurList).hasSize(databaseSizeBeforeCreate + 1);
        Conducteur testConducteur = conducteurList.get(conducteurList.size() - 1);
        assertThat(testConducteur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testConducteur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testConducteur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testConducteur.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testConducteur.getPermisNum()).isEqualTo(DEFAULT_PERMIS_NUM);
        assertThat(testConducteur.getLivreeLe()).isEqualTo(DEFAULT_LIVREE_LE);
        assertThat(testConducteur.getAddresse()).isEqualTo(DEFAULT_ADDRESSE);

        // Validate the Conducteur in Elasticsearch
        Conducteur conducteurEs = conducteurSearchRepository.findOne(testConducteur.getId());
        assertThat(conducteurEs).isEqualToComparingFieldByField(testConducteur);
    }

    @Test
    @Transactional
    public void createConducteurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = conducteurRepository.findAll().size();

        // Create the Conducteur with an existing ID
        conducteur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConducteurMockMvc.perform(post("/api/conducteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conducteur)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Conducteur> conducteurList = conducteurRepository.findAll();
        assertThat(conducteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllConducteurs() throws Exception {
        // Initialize the database
        conducteurRepository.saveAndFlush(conducteur);

        // Get all the conducteurList
        restConducteurMockMvc.perform(get("/api/conducteurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conducteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].permisNum").value(hasItem(DEFAULT_PERMIS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].livreeLe").value(hasItem(sameInstant(DEFAULT_LIVREE_LE))))
            .andExpect(jsonPath("$.[*].addresse").value(hasItem(DEFAULT_ADDRESSE.toString())));
    }

    @Test
    @Transactional
    public void getConducteur() throws Exception {
        // Initialize the database
        conducteurRepository.saveAndFlush(conducteur);

        // Get the conducteur
        restConducteurMockMvc.perform(get("/api/conducteurs/{id}", conducteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conducteur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.permisNum").value(DEFAULT_PERMIS_NUM.intValue()))
            .andExpect(jsonPath("$.livreeLe").value(sameInstant(DEFAULT_LIVREE_LE)))
            .andExpect(jsonPath("$.addresse").value(DEFAULT_ADDRESSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConducteur() throws Exception {
        // Get the conducteur
        restConducteurMockMvc.perform(get("/api/conducteurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConducteur() throws Exception {
        // Initialize the database
        conducteurRepository.saveAndFlush(conducteur);
        conducteurSearchRepository.save(conducteur);
        int databaseSizeBeforeUpdate = conducteurRepository.findAll().size();

        // Update the conducteur
        Conducteur updatedConducteur = conducteurRepository.findOne(conducteur.getId());
        updatedConducteur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL)
            .permisNum(UPDATED_PERMIS_NUM)
            .livreeLe(UPDATED_LIVREE_LE)
            .addresse(UPDATED_ADDRESSE);

        restConducteurMockMvc.perform(put("/api/conducteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConducteur)))
            .andExpect(status().isOk());

        // Validate the Conducteur in the database
        List<Conducteur> conducteurList = conducteurRepository.findAll();
        assertThat(conducteurList).hasSize(databaseSizeBeforeUpdate);
        Conducteur testConducteur = conducteurList.get(conducteurList.size() - 1);
        assertThat(testConducteur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testConducteur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testConducteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testConducteur.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testConducteur.getPermisNum()).isEqualTo(UPDATED_PERMIS_NUM);
        assertThat(testConducteur.getLivreeLe()).isEqualTo(UPDATED_LIVREE_LE);
        assertThat(testConducteur.getAddresse()).isEqualTo(UPDATED_ADDRESSE);

        // Validate the Conducteur in Elasticsearch
        Conducteur conducteurEs = conducteurSearchRepository.findOne(testConducteur.getId());
        assertThat(conducteurEs).isEqualToComparingFieldByField(testConducteur);
    }

    @Test
    @Transactional
    public void updateNonExistingConducteur() throws Exception {
        int databaseSizeBeforeUpdate = conducteurRepository.findAll().size();

        // Create the Conducteur

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConducteurMockMvc.perform(put("/api/conducteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conducteur)))
            .andExpect(status().isCreated());

        // Validate the Conducteur in the database
        List<Conducteur> conducteurList = conducteurRepository.findAll();
        assertThat(conducteurList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConducteur() throws Exception {
        // Initialize the database
        conducteurRepository.saveAndFlush(conducteur);
        conducteurSearchRepository.save(conducteur);
        int databaseSizeBeforeDelete = conducteurRepository.findAll().size();

        // Get the conducteur
        restConducteurMockMvc.perform(delete("/api/conducteurs/{id}", conducteur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean conducteurExistsInEs = conducteurSearchRepository.exists(conducteur.getId());
        assertThat(conducteurExistsInEs).isFalse();

        // Validate the database is empty
        List<Conducteur> conducteurList = conducteurRepository.findAll();
        assertThat(conducteurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchConducteur() throws Exception {
        // Initialize the database
        conducteurRepository.saveAndFlush(conducteur);
        conducteurSearchRepository.save(conducteur);

        // Search the conducteur
        restConducteurMockMvc.perform(get("/api/_search/conducteurs?query=id:" + conducteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conducteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].permisNum").value(hasItem(DEFAULT_PERMIS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].livreeLe").value(hasItem(sameInstant(DEFAULT_LIVREE_LE))))
            .andExpect(jsonPath("$.[*].addresse").value(hasItem(DEFAULT_ADDRESSE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conducteur.class);
        Conducteur conducteur1 = new Conducteur();
        conducteur1.setId(1L);
        Conducteur conducteur2 = new Conducteur();
        conducteur2.setId(conducteur1.getId());
        assertThat(conducteur1).isEqualTo(conducteur2);
        conducteur2.setId(2L);
        assertThat(conducteur1).isNotEqualTo(conducteur2);
        conducteur1.setId(null);
        assertThat(conducteur1).isNotEqualTo(conducteur2);
    }
}
