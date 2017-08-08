package com.viathings.smartstatement.web.rest;

import com.viathings.smartstatement.SmartStatementApp;

import com.viathings.smartstatement.domain.Constat;
import com.viathings.smartstatement.repository.ConstatRepository;
import com.viathings.smartstatement.repository.search.ConstatSearchRepository;
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

import com.viathings.smartstatement.domain.enumeration.YesNo;
import com.viathings.smartstatement.domain.enumeration.YesNo;
import com.viathings.smartstatement.domain.enumeration.YesNo;
/**
 * Test class for the ConstatResource REST controller.
 *
 * @see ConstatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartStatementApp.class)
public class ConstatResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_CONSTAT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CONSTAT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LIEU_CONSTAT = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_CONSTAT = "BBBBBBBBBB";

    private static final String DEFAULT_TEMOINS = "AAAAAAAAAA";
    private static final String UPDATED_TEMOINS = "BBBBBBBBBB";

    private static final YesNo DEFAULT_DEGATS = YesNo.YES;
    private static final YesNo UPDATED_DEGATS = YesNo.NO;

    private static final YesNo DEFAULT_DEGATS_APPARENT = YesNo.YES;
    private static final YesNo UPDATED_DEGATS_APPARENT = YesNo.NO;

    private static final YesNo DEFAULT_BLESSES = YesNo.YES;
    private static final YesNo UPDATED_BLESSES = YesNo.NO;

    private static final String DEFAULT_POINT_DE_CHOC_INITIAL_A = "AAAAAAAAAA";
    private static final String UPDATED_POINT_DE_CHOC_INITIAL_A = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATIONS_A = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS_A = "BBBBBBBBBB";

    private static final String DEFAULT_POINT_DE_CHOC_INITIAL_B = "AAAAAAAAAA";
    private static final String UPDATED_POINT_DE_CHOC_INITIAL_B = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATIONS_B = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS_B = "BBBBBBBBBB";

    private static final String DEFAULT_SENS_SUIVI_A = "AAAAAAAAAA";
    private static final String UPDATED_SENS_SUIVI_A = "BBBBBBBBBB";

    private static final String DEFAULT_VENANT_DE_A = "AAAAAAAAAA";
    private static final String UPDATED_VENANT_DE_A = "BBBBBBBBBB";

    private static final String DEFAULT_ALLANT_AA = "AAAAAAAAAA";
    private static final String UPDATED_ALLANT_AA = "BBBBBBBBBB";

    private static final String DEFAULT_SENS_SUIVI_B = "AAAAAAAAAA";
    private static final String UPDATED_SENS_SUIVI_B = "BBBBBBBBBB";

    private static final String DEFAULT_VENANT_DE_B = "AAAAAAAAAA";
    private static final String UPDATED_VENANT_DE_B = "BBBBBBBBBB";

    private static final String DEFAULT_ALLANT_AB = "AAAAAAAAAA";
    private static final String UPDATED_ALLANT_AB = "BBBBBBBBBB";

    @Autowired
    private ConstatRepository constatRepository;

    @Autowired
    private ConstatSearchRepository constatSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConstatMockMvc;

    private Constat constat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConstatResource constatResource = new ConstatResource(constatRepository, constatSearchRepository);
        this.restConstatMockMvc = MockMvcBuilders.standaloneSetup(constatResource)
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
    public static Constat createEntity(EntityManager em) {
        Constat constat = new Constat()
            .dateConstat(DEFAULT_DATE_CONSTAT)
            .lieuConstat(DEFAULT_LIEU_CONSTAT)
            .temoins(DEFAULT_TEMOINS)
            .degats(DEFAULT_DEGATS)
            .degatsApparent(DEFAULT_DEGATS_APPARENT)
            .blesses(DEFAULT_BLESSES)
            .pointDeChocInitialA(DEFAULT_POINT_DE_CHOC_INITIAL_A)
            .observationsA(DEFAULT_OBSERVATIONS_A)
            .pointDeChocInitialB(DEFAULT_POINT_DE_CHOC_INITIAL_B)
            .observationsB(DEFAULT_OBSERVATIONS_B)
            .sensSuiviA(DEFAULT_SENS_SUIVI_A)
            .venantDeA(DEFAULT_VENANT_DE_A)
            .allantAA(DEFAULT_ALLANT_AA)
            .sensSuiviB(DEFAULT_SENS_SUIVI_B)
            .venantDeB(DEFAULT_VENANT_DE_B)
            .allantAB(DEFAULT_ALLANT_AB);
        return constat;
    }

    @Before
    public void initTest() {
        constatSearchRepository.deleteAll();
        constat = createEntity(em);
    }

    @Test
    @Transactional
    public void createConstat() throws Exception {
        int databaseSizeBeforeCreate = constatRepository.findAll().size();

        // Create the Constat
        restConstatMockMvc.perform(post("/api/constats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(constat)))
            .andExpect(status().isCreated());

        // Validate the Constat in the database
        List<Constat> constatList = constatRepository.findAll();
        assertThat(constatList).hasSize(databaseSizeBeforeCreate + 1);
        Constat testConstat = constatList.get(constatList.size() - 1);
        assertThat(testConstat.getDateConstat()).isEqualTo(DEFAULT_DATE_CONSTAT);
        assertThat(testConstat.getLieuConstat()).isEqualTo(DEFAULT_LIEU_CONSTAT);
        assertThat(testConstat.getTemoins()).isEqualTo(DEFAULT_TEMOINS);
        assertThat(testConstat.getDegats()).isEqualTo(DEFAULT_DEGATS);
        assertThat(testConstat.getDegatsApparent()).isEqualTo(DEFAULT_DEGATS_APPARENT);
        assertThat(testConstat.getBlesses()).isEqualTo(DEFAULT_BLESSES);
        assertThat(testConstat.getPointDeChocInitialA()).isEqualTo(DEFAULT_POINT_DE_CHOC_INITIAL_A);
        assertThat(testConstat.getObservationsA()).isEqualTo(DEFAULT_OBSERVATIONS_A);
        assertThat(testConstat.getPointDeChocInitialB()).isEqualTo(DEFAULT_POINT_DE_CHOC_INITIAL_B);
        assertThat(testConstat.getObservationsB()).isEqualTo(DEFAULT_OBSERVATIONS_B);
        assertThat(testConstat.getSensSuiviA()).isEqualTo(DEFAULT_SENS_SUIVI_A);
        assertThat(testConstat.getVenantDeA()).isEqualTo(DEFAULT_VENANT_DE_A);
        assertThat(testConstat.getAllantAA()).isEqualTo(DEFAULT_ALLANT_AA);
        assertThat(testConstat.getSensSuiviB()).isEqualTo(DEFAULT_SENS_SUIVI_B);
        assertThat(testConstat.getVenantDeB()).isEqualTo(DEFAULT_VENANT_DE_B);
        assertThat(testConstat.getAllantAB()).isEqualTo(DEFAULT_ALLANT_AB);

        // Validate the Constat in Elasticsearch
        Constat constatEs = constatSearchRepository.findOne(testConstat.getId());
        assertThat(constatEs).isEqualToComparingFieldByField(testConstat);
    }

    @Test
    @Transactional
    public void createConstatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = constatRepository.findAll().size();

        // Create the Constat with an existing ID
        constat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConstatMockMvc.perform(post("/api/constats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(constat)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Constat> constatList = constatRepository.findAll();
        assertThat(constatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllConstats() throws Exception {
        // Initialize the database
        constatRepository.saveAndFlush(constat);

        // Get all the constatList
        restConstatMockMvc.perform(get("/api/constats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(constat.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateConstat").value(hasItem(sameInstant(DEFAULT_DATE_CONSTAT))))
            .andExpect(jsonPath("$.[*].lieuConstat").value(hasItem(DEFAULT_LIEU_CONSTAT.toString())))
            .andExpect(jsonPath("$.[*].temoins").value(hasItem(DEFAULT_TEMOINS.toString())))
            .andExpect(jsonPath("$.[*].degats").value(hasItem(DEFAULT_DEGATS.toString())))
            .andExpect(jsonPath("$.[*].degatsApparent").value(hasItem(DEFAULT_DEGATS_APPARENT.toString())))
            .andExpect(jsonPath("$.[*].blesses").value(hasItem(DEFAULT_BLESSES.toString())))
            .andExpect(jsonPath("$.[*].pointDeChocInitialA").value(hasItem(DEFAULT_POINT_DE_CHOC_INITIAL_A.toString())))
            .andExpect(jsonPath("$.[*].observationsA").value(hasItem(DEFAULT_OBSERVATIONS_A.toString())))
            .andExpect(jsonPath("$.[*].pointDeChocInitialB").value(hasItem(DEFAULT_POINT_DE_CHOC_INITIAL_B.toString())))
            .andExpect(jsonPath("$.[*].observationsB").value(hasItem(DEFAULT_OBSERVATIONS_B.toString())))
            .andExpect(jsonPath("$.[*].sensSuiviA").value(hasItem(DEFAULT_SENS_SUIVI_A.toString())))
            .andExpect(jsonPath("$.[*].venantDeA").value(hasItem(DEFAULT_VENANT_DE_A.toString())))
            .andExpect(jsonPath("$.[*].allantAA").value(hasItem(DEFAULT_ALLANT_AA.toString())))
            .andExpect(jsonPath("$.[*].sensSuiviB").value(hasItem(DEFAULT_SENS_SUIVI_B.toString())))
            .andExpect(jsonPath("$.[*].venantDeB").value(hasItem(DEFAULT_VENANT_DE_B.toString())))
            .andExpect(jsonPath("$.[*].allantAB").value(hasItem(DEFAULT_ALLANT_AB.toString())));
    }

    @Test
    @Transactional
    public void getConstat() throws Exception {
        // Initialize the database
        constatRepository.saveAndFlush(constat);

        // Get the constat
        restConstatMockMvc.perform(get("/api/constats/{id}", constat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(constat.getId().intValue()))
            .andExpect(jsonPath("$.dateConstat").value(sameInstant(DEFAULT_DATE_CONSTAT)))
            .andExpect(jsonPath("$.lieuConstat").value(DEFAULT_LIEU_CONSTAT.toString()))
            .andExpect(jsonPath("$.temoins").value(DEFAULT_TEMOINS.toString()))
            .andExpect(jsonPath("$.degats").value(DEFAULT_DEGATS.toString()))
            .andExpect(jsonPath("$.degatsApparent").value(DEFAULT_DEGATS_APPARENT.toString()))
            .andExpect(jsonPath("$.blesses").value(DEFAULT_BLESSES.toString()))
            .andExpect(jsonPath("$.pointDeChocInitialA").value(DEFAULT_POINT_DE_CHOC_INITIAL_A.toString()))
            .andExpect(jsonPath("$.observationsA").value(DEFAULT_OBSERVATIONS_A.toString()))
            .andExpect(jsonPath("$.pointDeChocInitialB").value(DEFAULT_POINT_DE_CHOC_INITIAL_B.toString()))
            .andExpect(jsonPath("$.observationsB").value(DEFAULT_OBSERVATIONS_B.toString()))
            .andExpect(jsonPath("$.sensSuiviA").value(DEFAULT_SENS_SUIVI_A.toString()))
            .andExpect(jsonPath("$.venantDeA").value(DEFAULT_VENANT_DE_A.toString()))
            .andExpect(jsonPath("$.allantAA").value(DEFAULT_ALLANT_AA.toString()))
            .andExpect(jsonPath("$.sensSuiviB").value(DEFAULT_SENS_SUIVI_B.toString()))
            .andExpect(jsonPath("$.venantDeB").value(DEFAULT_VENANT_DE_B.toString()))
            .andExpect(jsonPath("$.allantAB").value(DEFAULT_ALLANT_AB.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConstat() throws Exception {
        // Get the constat
        restConstatMockMvc.perform(get("/api/constats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConstat() throws Exception {
        // Initialize the database
        constatRepository.saveAndFlush(constat);
        constatSearchRepository.save(constat);
        int databaseSizeBeforeUpdate = constatRepository.findAll().size();

        // Update the constat
        Constat updatedConstat = constatRepository.findOne(constat.getId());
        updatedConstat
            .dateConstat(UPDATED_DATE_CONSTAT)
            .lieuConstat(UPDATED_LIEU_CONSTAT)
            .temoins(UPDATED_TEMOINS)
            .degats(UPDATED_DEGATS)
            .degatsApparent(UPDATED_DEGATS_APPARENT)
            .blesses(UPDATED_BLESSES)
            .pointDeChocInitialA(UPDATED_POINT_DE_CHOC_INITIAL_A)
            .observationsA(UPDATED_OBSERVATIONS_A)
            .pointDeChocInitialB(UPDATED_POINT_DE_CHOC_INITIAL_B)
            .observationsB(UPDATED_OBSERVATIONS_B)
            .sensSuiviA(UPDATED_SENS_SUIVI_A)
            .venantDeA(UPDATED_VENANT_DE_A)
            .allantAA(UPDATED_ALLANT_AA)
            .sensSuiviB(UPDATED_SENS_SUIVI_B)
            .venantDeB(UPDATED_VENANT_DE_B)
            .allantAB(UPDATED_ALLANT_AB);

        restConstatMockMvc.perform(put("/api/constats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConstat)))
            .andExpect(status().isOk());

        // Validate the Constat in the database
        List<Constat> constatList = constatRepository.findAll();
        assertThat(constatList).hasSize(databaseSizeBeforeUpdate);
        Constat testConstat = constatList.get(constatList.size() - 1);
        assertThat(testConstat.getDateConstat()).isEqualTo(UPDATED_DATE_CONSTAT);
        assertThat(testConstat.getLieuConstat()).isEqualTo(UPDATED_LIEU_CONSTAT);
        assertThat(testConstat.getTemoins()).isEqualTo(UPDATED_TEMOINS);
        assertThat(testConstat.getDegats()).isEqualTo(UPDATED_DEGATS);
        assertThat(testConstat.getDegatsApparent()).isEqualTo(UPDATED_DEGATS_APPARENT);
        assertThat(testConstat.getBlesses()).isEqualTo(UPDATED_BLESSES);
        assertThat(testConstat.getPointDeChocInitialA()).isEqualTo(UPDATED_POINT_DE_CHOC_INITIAL_A);
        assertThat(testConstat.getObservationsA()).isEqualTo(UPDATED_OBSERVATIONS_A);
        assertThat(testConstat.getPointDeChocInitialB()).isEqualTo(UPDATED_POINT_DE_CHOC_INITIAL_B);
        assertThat(testConstat.getObservationsB()).isEqualTo(UPDATED_OBSERVATIONS_B);
        assertThat(testConstat.getSensSuiviA()).isEqualTo(UPDATED_SENS_SUIVI_A);
        assertThat(testConstat.getVenantDeA()).isEqualTo(UPDATED_VENANT_DE_A);
        assertThat(testConstat.getAllantAA()).isEqualTo(UPDATED_ALLANT_AA);
        assertThat(testConstat.getSensSuiviB()).isEqualTo(UPDATED_SENS_SUIVI_B);
        assertThat(testConstat.getVenantDeB()).isEqualTo(UPDATED_VENANT_DE_B);
        assertThat(testConstat.getAllantAB()).isEqualTo(UPDATED_ALLANT_AB);

        // Validate the Constat in Elasticsearch
        Constat constatEs = constatSearchRepository.findOne(testConstat.getId());
        assertThat(constatEs).isEqualToComparingFieldByField(testConstat);
    }

    @Test
    @Transactional
    public void updateNonExistingConstat() throws Exception {
        int databaseSizeBeforeUpdate = constatRepository.findAll().size();

        // Create the Constat

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConstatMockMvc.perform(put("/api/constats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(constat)))
            .andExpect(status().isCreated());

        // Validate the Constat in the database
        List<Constat> constatList = constatRepository.findAll();
        assertThat(constatList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConstat() throws Exception {
        // Initialize the database
        constatRepository.saveAndFlush(constat);
        constatSearchRepository.save(constat);
        int databaseSizeBeforeDelete = constatRepository.findAll().size();

        // Get the constat
        restConstatMockMvc.perform(delete("/api/constats/{id}", constat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean constatExistsInEs = constatSearchRepository.exists(constat.getId());
        assertThat(constatExistsInEs).isFalse();

        // Validate the database is empty
        List<Constat> constatList = constatRepository.findAll();
        assertThat(constatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchConstat() throws Exception {
        // Initialize the database
        constatRepository.saveAndFlush(constat);
        constatSearchRepository.save(constat);

        // Search the constat
        restConstatMockMvc.perform(get("/api/_search/constats?query=id:" + constat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(constat.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateConstat").value(hasItem(sameInstant(DEFAULT_DATE_CONSTAT))))
            .andExpect(jsonPath("$.[*].lieuConstat").value(hasItem(DEFAULT_LIEU_CONSTAT.toString())))
            .andExpect(jsonPath("$.[*].temoins").value(hasItem(DEFAULT_TEMOINS.toString())))
            .andExpect(jsonPath("$.[*].degats").value(hasItem(DEFAULT_DEGATS.toString())))
            .andExpect(jsonPath("$.[*].degatsApparent").value(hasItem(DEFAULT_DEGATS_APPARENT.toString())))
            .andExpect(jsonPath("$.[*].blesses").value(hasItem(DEFAULT_BLESSES.toString())))
            .andExpect(jsonPath("$.[*].pointDeChocInitialA").value(hasItem(DEFAULT_POINT_DE_CHOC_INITIAL_A.toString())))
            .andExpect(jsonPath("$.[*].observationsA").value(hasItem(DEFAULT_OBSERVATIONS_A.toString())))
            .andExpect(jsonPath("$.[*].pointDeChocInitialB").value(hasItem(DEFAULT_POINT_DE_CHOC_INITIAL_B.toString())))
            .andExpect(jsonPath("$.[*].observationsB").value(hasItem(DEFAULT_OBSERVATIONS_B.toString())))
            .andExpect(jsonPath("$.[*].sensSuiviA").value(hasItem(DEFAULT_SENS_SUIVI_A.toString())))
            .andExpect(jsonPath("$.[*].venantDeA").value(hasItem(DEFAULT_VENANT_DE_A.toString())))
            .andExpect(jsonPath("$.[*].allantAA").value(hasItem(DEFAULT_ALLANT_AA.toString())))
            .andExpect(jsonPath("$.[*].sensSuiviB").value(hasItem(DEFAULT_SENS_SUIVI_B.toString())))
            .andExpect(jsonPath("$.[*].venantDeB").value(hasItem(DEFAULT_VENANT_DE_B.toString())))
            .andExpect(jsonPath("$.[*].allantAB").value(hasItem(DEFAULT_ALLANT_AB.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Constat.class);
        Constat constat1 = new Constat();
        constat1.setId(1L);
        Constat constat2 = new Constat();
        constat2.setId(constat1.getId());
        assertThat(constat1).isEqualTo(constat2);
        constat2.setId(2L);
        assertThat(constat1).isNotEqualTo(constat2);
        constat1.setId(null);
        assertThat(constat1).isNotEqualTo(constat2);
    }
}
