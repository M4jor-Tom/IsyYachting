package com.m4jortom.isyyachting.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.m4jortom.isyyachting.IntegrationTest;
import com.m4jortom.isyyachting.domain.Rental;
import com.m4jortom.isyyachting.domain.RentalPrice;
import com.m4jortom.isyyachting.domain.enumeration.Season;
import com.m4jortom.isyyachting.repository.RentalPriceRepository;
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
 * Integration tests for the {@link RentalPriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RentalPriceResourceIT {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final Season DEFAULT_SEASON = Season.LOW;
    private static final Season UPDATED_SEASON = Season.MIDDLE;

    private static final String ENTITY_API_URL = "/api/rental-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RentalPriceRepository rentalPriceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentalPriceMockMvc;

    private RentalPrice rentalPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentalPrice createEntity(EntityManager em) {
        RentalPrice rentalPrice = new RentalPrice().value(DEFAULT_VALUE).season(DEFAULT_SEASON);
        // Add required entity
        Rental rental;
        if (TestUtil.findAll(em, Rental.class).isEmpty()) {
            rental = RentalResourceIT.createEntity(em);
            em.persist(rental);
            em.flush();
        } else {
            rental = TestUtil.findAll(em, Rental.class).get(0);
        }
        rentalPrice.setRental(rental);
        return rentalPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentalPrice createUpdatedEntity(EntityManager em) {
        RentalPrice rentalPrice = new RentalPrice().value(UPDATED_VALUE).season(UPDATED_SEASON);
        // Add required entity
        Rental rental;
        if (TestUtil.findAll(em, Rental.class).isEmpty()) {
            rental = RentalResourceIT.createUpdatedEntity(em);
            em.persist(rental);
            em.flush();
        } else {
            rental = TestUtil.findAll(em, Rental.class).get(0);
        }
        rentalPrice.setRental(rental);
        return rentalPrice;
    }

    @BeforeEach
    public void initTest() {
        rentalPrice = createEntity(em);
    }

    @Test
    @Transactional
    void createRentalPrice() throws Exception {
        int databaseSizeBeforeCreate = rentalPriceRepository.findAll().size();
        // Create the RentalPrice
        restRentalPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalPrice)))
            .andExpect(status().isCreated());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeCreate + 1);
        RentalPrice testRentalPrice = rentalPriceList.get(rentalPriceList.size() - 1);
        assertThat(testRentalPrice.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testRentalPrice.getSeason()).isEqualTo(DEFAULT_SEASON);
    }

    @Test
    @Transactional
    void createRentalPriceWithExistingId() throws Exception {
        // Create the RentalPrice with an existing ID
        rentalPrice.setId(1L);

        int databaseSizeBeforeCreate = rentalPriceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalPrice)))
            .andExpect(status().isBadRequest());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalPriceRepository.findAll().size();
        // set the field null
        rentalPrice.setValue(null);

        // Create the RentalPrice, which fails.

        restRentalPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalPrice)))
            .andExpect(status().isBadRequest());

        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalPriceRepository.findAll().size();
        // set the field null
        rentalPrice.setSeason(null);

        // Create the RentalPrice, which fails.

        restRentalPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalPrice)))
            .andExpect(status().isBadRequest());

        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRentalPrices() throws Exception {
        // Initialize the database
        rentalPriceRepository.saveAndFlush(rentalPrice);

        // Get all the rentalPriceList
        restRentalPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentalPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON.toString())));
    }

    @Test
    @Transactional
    void getRentalPrice() throws Exception {
        // Initialize the database
        rentalPriceRepository.saveAndFlush(rentalPrice);

        // Get the rentalPrice
        restRentalPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, rentalPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rentalPrice.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRentalPrice() throws Exception {
        // Get the rentalPrice
        restRentalPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRentalPrice() throws Exception {
        // Initialize the database
        rentalPriceRepository.saveAndFlush(rentalPrice);

        int databaseSizeBeforeUpdate = rentalPriceRepository.findAll().size();

        // Update the rentalPrice
        RentalPrice updatedRentalPrice = rentalPriceRepository.findById(rentalPrice.getId()).get();
        // Disconnect from session so that the updates on updatedRentalPrice are not directly saved in db
        em.detach(updatedRentalPrice);
        updatedRentalPrice.value(UPDATED_VALUE).season(UPDATED_SEASON);

        restRentalPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRentalPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRentalPrice))
            )
            .andExpect(status().isOk());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeUpdate);
        RentalPrice testRentalPrice = rentalPriceList.get(rentalPriceList.size() - 1);
        assertThat(testRentalPrice.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testRentalPrice.getSeason()).isEqualTo(UPDATED_SEASON);
    }

    @Test
    @Transactional
    void putNonExistingRentalPrice() throws Exception {
        int databaseSizeBeforeUpdate = rentalPriceRepository.findAll().size();
        rentalPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentalPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRentalPrice() throws Exception {
        int databaseSizeBeforeUpdate = rentalPriceRepository.findAll().size();
        rentalPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRentalPrice() throws Exception {
        int databaseSizeBeforeUpdate = rentalPriceRepository.findAll().size();
        rentalPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalPriceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentalPrice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRentalPriceWithPatch() throws Exception {
        // Initialize the database
        rentalPriceRepository.saveAndFlush(rentalPrice);

        int databaseSizeBeforeUpdate = rentalPriceRepository.findAll().size();

        // Update the rentalPrice using partial update
        RentalPrice partialUpdatedRentalPrice = new RentalPrice();
        partialUpdatedRentalPrice.setId(rentalPrice.getId());

        restRentalPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRentalPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRentalPrice))
            )
            .andExpect(status().isOk());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeUpdate);
        RentalPrice testRentalPrice = rentalPriceList.get(rentalPriceList.size() - 1);
        assertThat(testRentalPrice.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testRentalPrice.getSeason()).isEqualTo(DEFAULT_SEASON);
    }

    @Test
    @Transactional
    void fullUpdateRentalPriceWithPatch() throws Exception {
        // Initialize the database
        rentalPriceRepository.saveAndFlush(rentalPrice);

        int databaseSizeBeforeUpdate = rentalPriceRepository.findAll().size();

        // Update the rentalPrice using partial update
        RentalPrice partialUpdatedRentalPrice = new RentalPrice();
        partialUpdatedRentalPrice.setId(rentalPrice.getId());

        partialUpdatedRentalPrice.value(UPDATED_VALUE).season(UPDATED_SEASON);

        restRentalPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRentalPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRentalPrice))
            )
            .andExpect(status().isOk());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeUpdate);
        RentalPrice testRentalPrice = rentalPriceList.get(rentalPriceList.size() - 1);
        assertThat(testRentalPrice.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testRentalPrice.getSeason()).isEqualTo(UPDATED_SEASON);
    }

    @Test
    @Transactional
    void patchNonExistingRentalPrice() throws Exception {
        int databaseSizeBeforeUpdate = rentalPriceRepository.findAll().size();
        rentalPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rentalPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRentalPrice() throws Exception {
        int databaseSizeBeforeUpdate = rentalPriceRepository.findAll().size();
        rentalPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRentalPrice() throws Exception {
        int databaseSizeBeforeUpdate = rentalPriceRepository.findAll().size();
        rentalPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalPriceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rentalPrice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RentalPrice in the database
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRentalPrice() throws Exception {
        // Initialize the database
        rentalPriceRepository.saveAndFlush(rentalPrice);

        int databaseSizeBeforeDelete = rentalPriceRepository.findAll().size();

        // Delete the rentalPrice
        restRentalPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, rentalPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RentalPrice> rentalPriceList = rentalPriceRepository.findAll();
        assertThat(rentalPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
