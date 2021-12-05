package com.m4jortom.isyyachting.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.m4jortom.isyyachting.IntegrationTest;
import com.m4jortom.isyyachting.domain.Rental;
import com.m4jortom.isyyachting.repository.RentalRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RentalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RentalResourceIT {

    private static final Instant DEFAULT_BEGINING_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BEGINING_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENDING_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENDING_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/rentals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentalMockMvc;

    private Rental rental;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createEntity(EntityManager em) {
        Rental rental = new Rental().beginingInstant(DEFAULT_BEGINING_INSTANT).endingInstant(DEFAULT_ENDING_INSTANT);
        return rental;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createUpdatedEntity(EntityManager em) {
        Rental rental = new Rental().beginingInstant(UPDATED_BEGINING_INSTANT).endingInstant(UPDATED_ENDING_INSTANT);
        return rental;
    }

    @BeforeEach
    public void initTest() {
        rental = createEntity(em);
    }

    @Test
    @Transactional
    void createRental() throws Exception {
        int databaseSizeBeforeCreate = rentalRepository.findAll().size();
        // Create the Rental
        restRentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rental)))
            .andExpect(status().isCreated());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeCreate + 1);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getBeginingInstant()).isEqualTo(DEFAULT_BEGINING_INSTANT);
        assertThat(testRental.getEndingInstant()).isEqualTo(DEFAULT_ENDING_INSTANT);
    }

    @Test
    @Transactional
    void createRentalWithExistingId() throws Exception {
        // Create the Rental with an existing ID
        rental.setId(1L);

        int databaseSizeBeforeCreate = rentalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rental)))
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBeginingInstantIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalRepository.findAll().size();
        // set the field null
        rental.setBeginingInstant(null);

        // Create the Rental, which fails.

        restRentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rental)))
            .andExpect(status().isBadRequest());

        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndingInstantIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalRepository.findAll().size();
        // set the field null
        rental.setEndingInstant(null);

        // Create the Rental, which fails.

        restRentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rental)))
            .andExpect(status().isBadRequest());

        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRentals() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rental.getId().intValue())))
            .andExpect(jsonPath("$.[*].beginingInstant").value(hasItem(DEFAULT_BEGINING_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].endingInstant").value(hasItem(DEFAULT_ENDING_INSTANT.toString())));
    }

    @Test
    @Transactional
    void getRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get the rental
        restRentalMockMvc
            .perform(get(ENTITY_API_URL_ID, rental.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rental.getId().intValue()))
            .andExpect(jsonPath("$.beginingInstant").value(DEFAULT_BEGINING_INSTANT.toString()))
            .andExpect(jsonPath("$.endingInstant").value(DEFAULT_ENDING_INSTANT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRental() throws Exception {
        // Get the rental
        restRentalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental
        Rental updatedRental = rentalRepository.findById(rental.getId()).get();
        // Disconnect from session so that the updates on updatedRental are not directly saved in db
        em.detach(updatedRental);
        updatedRental.beginingInstant(UPDATED_BEGINING_INSTANT).endingInstant(UPDATED_ENDING_INSTANT);

        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRental.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getBeginingInstant()).isEqualTo(UPDATED_BEGINING_INSTANT);
        assertThat(testRental.getEndingInstant()).isEqualTo(UPDATED_ENDING_INSTANT);
    }

    @Test
    @Transactional
    void putNonExistingRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rental.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rental))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rental))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rental)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRentalWithPatch() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental using partial update
        Rental partialUpdatedRental = new Rental();
        partialUpdatedRental.setId(rental.getId());

        partialUpdatedRental.endingInstant(UPDATED_ENDING_INSTANT);

        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRental.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getBeginingInstant()).isEqualTo(DEFAULT_BEGINING_INSTANT);
        assertThat(testRental.getEndingInstant()).isEqualTo(UPDATED_ENDING_INSTANT);
    }

    @Test
    @Transactional
    void fullUpdateRentalWithPatch() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental using partial update
        Rental partialUpdatedRental = new Rental();
        partialUpdatedRental.setId(rental.getId());

        partialUpdatedRental.beginingInstant(UPDATED_BEGINING_INSTANT).endingInstant(UPDATED_ENDING_INSTANT);

        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRental.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getBeginingInstant()).isEqualTo(UPDATED_BEGINING_INSTANT);
        assertThat(testRental.getEndingInstant()).isEqualTo(UPDATED_ENDING_INSTANT);
    }

    @Test
    @Transactional
    void patchNonExistingRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rental.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rental))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rental))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rental)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeDelete = rentalRepository.findAll().size();

        // Delete the rental
        restRentalMockMvc
            .perform(delete(ENTITY_API_URL_ID, rental.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
