package com.m4jortom.isyyachting.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.m4jortom.isyyachting.IntegrationTest;
import com.m4jortom.isyyachting.domain.Day;
import com.m4jortom.isyyachting.domain.enumeration.CancelingWeather;
import com.m4jortom.isyyachting.repository.DayRepository;
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
 * Integration tests for the {@link DayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DayResourceIT {

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final CancelingWeather DEFAULT_FORECASTED_WEATHER = CancelingWeather.TOO_WINDY;
    private static final CancelingWeather UPDATED_FORECASTED_WEATHER = CancelingWeather.TOO_RAINY;

    private static final String ENTITY_API_URL = "/api/days";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDayMockMvc;

    private Day day;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Day createEntity(EntityManager em) {
        Day day = new Day().time(DEFAULT_TIME).forecastedWeather(DEFAULT_FORECASTED_WEATHER);
        return day;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Day createUpdatedEntity(EntityManager em) {
        Day day = new Day().time(UPDATED_TIME).forecastedWeather(UPDATED_FORECASTED_WEATHER);
        return day;
    }

    @BeforeEach
    public void initTest() {
        day = createEntity(em);
    }

    @Test
    @Transactional
    void createDay() throws Exception {
        int databaseSizeBeforeCreate = dayRepository.findAll().size();
        // Create the Day
        restDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isCreated());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeCreate + 1);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testDay.getForecastedWeather()).isEqualTo(DEFAULT_FORECASTED_WEATHER);
    }

    @Test
    @Transactional
    void createDayWithExistingId() throws Exception {
        // Create the Day with an existing ID
        day.setId(1L);

        int databaseSizeBeforeCreate = dayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayRepository.findAll().size();
        // set the field null
        day.setTime(null);

        // Create the Day, which fails.

        restDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isBadRequest());

        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDays() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get all the dayList
        restDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(day.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].forecastedWeather").value(hasItem(DEFAULT_FORECASTED_WEATHER.toString())));
    }

    @Test
    @Transactional
    void getDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get the day
        restDayMockMvc
            .perform(get(ENTITY_API_URL_ID, day.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(day.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.forecastedWeather").value(DEFAULT_FORECASTED_WEATHER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDay() throws Exception {
        // Get the day
        restDayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Update the day
        Day updatedDay = dayRepository.findById(day.getId()).get();
        // Disconnect from session so that the updates on updatedDay are not directly saved in db
        em.detach(updatedDay);
        updatedDay.time(UPDATED_TIME).forecastedWeather(UPDATED_FORECASTED_WEATHER);

        restDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDay))
            )
            .andExpect(status().isOk());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testDay.getForecastedWeather()).isEqualTo(UPDATED_FORECASTED_WEATHER);
    }

    @Test
    @Transactional
    void putNonExistingDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, day.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(day))
            )
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(day))
            )
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDayWithPatch() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Update the day using partial update
        Day partialUpdatedDay = new Day();
        partialUpdatedDay.setId(day.getId());

        partialUpdatedDay.time(UPDATED_TIME);

        restDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDay))
            )
            .andExpect(status().isOk());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testDay.getForecastedWeather()).isEqualTo(DEFAULT_FORECASTED_WEATHER);
    }

    @Test
    @Transactional
    void fullUpdateDayWithPatch() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Update the day using partial update
        Day partialUpdatedDay = new Day();
        partialUpdatedDay.setId(day.getId());

        partialUpdatedDay.time(UPDATED_TIME).forecastedWeather(UPDATED_FORECASTED_WEATHER);

        restDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDay))
            )
            .andExpect(status().isOk());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testDay.getForecastedWeather()).isEqualTo(UPDATED_FORECASTED_WEATHER);
    }

    @Test
    @Transactional
    void patchNonExistingDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, day.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(day))
            )
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(day))
            )
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(day)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        int databaseSizeBeforeDelete = dayRepository.findAll().size();

        // Delete the day
        restDayMockMvc.perform(delete(ENTITY_API_URL_ID, day.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
