package org.khasanof.backup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.backup.domain.BackupTenantSettingAsserts.*;
import static org.khasanof.backup.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khasanof.backup.IntegrationTest;
import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.domain.common.enumeration.BackupTimeUnit;
import org.khasanof.backup.repository.common.BackupTenantSettingRepository;
import org.khasanof.backup.service.dto.BackupTenantSettingDTO;
import org.khasanof.backup.service.mapper.BackupTenantSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BackupTenantSettingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BackupTenantSettingResourceIT {

    private static final BackupTimeUnit DEFAULT_TIME_UNIT = BackupTimeUnit.HOUR;
    private static final BackupTimeUnit UPDATED_TIME_UNIT = BackupTimeUnit.DAY;

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;
    private static final Integer SMALLER_DURATION = 1 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/backup-tenant-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BackupTenantSettingRepository backupTenantSettingRepository;

    @Autowired
    private BackupTenantSettingMapper backupTenantSettingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBackupTenantSettingMockMvc;

    private BackupTenantSetting backupTenantSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupTenantSetting createEntity(EntityManager em) {
        BackupTenantSetting backupTenantSetting = new BackupTenantSetting()
            .timeUnit(DEFAULT_TIME_UNIT)
            .duration(DEFAULT_DURATION)
            .isActive(DEFAULT_IS_ACTIVE);
        return backupTenantSetting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupTenantSetting createUpdatedEntity(EntityManager em) {
        BackupTenantSetting backupTenantSetting = new BackupTenantSetting()
            .timeUnit(UPDATED_TIME_UNIT)
            .duration(UPDATED_DURATION)
            .isActive(UPDATED_IS_ACTIVE);
        return backupTenantSetting;
    }

    @BeforeEach
    public void initTest() {
        backupTenantSetting = createEntity(em);
    }

    @Test
    @Transactional
    void createBackupTenantSetting() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BackupTenantSetting
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);
        var returnedBackupTenantSettingDTO = om.readValue(
            restBackupTenantSettingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantSettingDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BackupTenantSettingDTO.class
        );

        // Validate the BackupTenantSetting in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBackupTenantSetting = backupTenantSettingMapper.toEntity(returnedBackupTenantSettingDTO);
        assertBackupTenantSettingUpdatableFieldsEquals(
            returnedBackupTenantSetting,
            getPersistedBackupTenantSetting(returnedBackupTenantSetting)
        );
    }

    @Test
    @Transactional
    void createBackupTenantSettingWithExistingId() throws Exception {
        // Create the BackupTenantSetting with an existing ID
        backupTenantSetting.setId(1L);
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBackupTenantSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantSettingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BackupTenantSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimeUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupTenantSetting.setTimeUnit(null);

        // Create the BackupTenantSetting, which fails.
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        restBackupTenantSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantSettingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupTenantSetting.setDuration(null);

        // Create the BackupTenantSetting, which fails.
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        restBackupTenantSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantSettingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupTenantSetting.setIsActive(null);

        // Create the BackupTenantSetting, which fails.
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        restBackupTenantSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantSettingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBackupTenantSettings() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList
        restBackupTenantSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupTenantSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeUnit").value(hasItem(DEFAULT_TIME_UNIT.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getBackupTenantSetting() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get the backupTenantSetting
        restBackupTenantSettingMockMvc
            .perform(get(ENTITY_API_URL_ID, backupTenantSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(backupTenantSetting.getId().intValue()))
            .andExpect(jsonPath("$.timeUnit").value(DEFAULT_TIME_UNIT.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getBackupTenantSettingsByIdFiltering() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        Long id = backupTenantSetting.getId();

        defaultBackupTenantSettingFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBackupTenantSettingFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBackupTenantSettingFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByTimeUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where timeUnit equals to
        defaultBackupTenantSettingFiltering("timeUnit.equals=" + DEFAULT_TIME_UNIT, "timeUnit.equals=" + UPDATED_TIME_UNIT);
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByTimeUnitIsInShouldWork() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where timeUnit in
        defaultBackupTenantSettingFiltering(
            "timeUnit.in=" + DEFAULT_TIME_UNIT + "," + UPDATED_TIME_UNIT,
            "timeUnit.in=" + UPDATED_TIME_UNIT
        );
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByTimeUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where timeUnit is not null
        defaultBackupTenantSettingFiltering("timeUnit.specified=true", "timeUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where duration equals to
        defaultBackupTenantSettingFiltering("duration.equals=" + DEFAULT_DURATION, "duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where duration in
        defaultBackupTenantSettingFiltering("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION, "duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where duration is not null
        defaultBackupTenantSettingFiltering("duration.specified=true", "duration.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where duration is greater than or equal to
        defaultBackupTenantSettingFiltering(
            "duration.greaterThanOrEqual=" + DEFAULT_DURATION,
            "duration.greaterThanOrEqual=" + UPDATED_DURATION
        );
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where duration is less than or equal to
        defaultBackupTenantSettingFiltering("duration.lessThanOrEqual=" + DEFAULT_DURATION, "duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where duration is less than
        defaultBackupTenantSettingFiltering("duration.lessThan=" + UPDATED_DURATION, "duration.lessThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where duration is greater than
        defaultBackupTenantSettingFiltering("duration.greaterThan=" + SMALLER_DURATION, "duration.greaterThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where isActive equals to
        defaultBackupTenantSettingFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where isActive in
        defaultBackupTenantSettingFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllBackupTenantSettingsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        // Get all the backupTenantSettingList where isActive is not null
        defaultBackupTenantSettingFiltering("isActive.specified=true", "isActive.specified=false");
    }

    private void defaultBackupTenantSettingFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBackupTenantSettingShouldBeFound(shouldBeFound);
        defaultBackupTenantSettingShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBackupTenantSettingShouldBeFound(String filter) throws Exception {
        restBackupTenantSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupTenantSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeUnit").value(hasItem(DEFAULT_TIME_UNIT.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBackupTenantSettingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBackupTenantSettingShouldNotBeFound(String filter) throws Exception {
        restBackupTenantSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBackupTenantSettingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBackupTenantSetting() throws Exception {
        // Get the backupTenantSetting
        restBackupTenantSettingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBackupTenantSetting() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupTenantSetting
        BackupTenantSetting updatedBackupTenantSetting = backupTenantSettingRepository.findById(backupTenantSetting.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBackupTenantSetting are not directly saved in db
        em.detach(updatedBackupTenantSetting);
        updatedBackupTenantSetting.timeUnit(UPDATED_TIME_UNIT).duration(UPDATED_DURATION).isActive(UPDATED_IS_ACTIVE);
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(updatedBackupTenantSetting);

        restBackupTenantSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupTenantSettingDTO))
            )
            .andExpect(status().isOk());

        // Validate the BackupTenantSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBackupTenantSettingToMatchAllProperties(updatedBackupTenantSetting);
    }

    @Test
    @Transactional
    void putNonExistingBackupTenantSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenantSetting.setId(longCount.incrementAndGet());

        // Create the BackupTenantSetting
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupTenantSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupTenantSettingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupTenantSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBackupTenantSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenantSetting.setId(longCount.incrementAndGet());

        // Create the BackupTenantSetting
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupTenantSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupTenantSettingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupTenantSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBackupTenantSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenantSetting.setId(longCount.incrementAndGet());

        // Create the BackupTenantSetting
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupTenantSettingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantSettingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupTenantSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void fullUpdateBackupTenantSettingWithPatch() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupTenantSetting using partial update
        BackupTenantSetting partialUpdatedBackupTenantSetting = new BackupTenantSetting();
        partialUpdatedBackupTenantSetting.setId(backupTenantSetting.getId());

        partialUpdatedBackupTenantSetting.timeUnit(UPDATED_TIME_UNIT).duration(UPDATED_DURATION).isActive(UPDATED_IS_ACTIVE);

        restBackupTenantSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBackupTenantSetting))
            )
            .andExpect(status().isOk());

        // Validate the BackupTenantSetting in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBackupTenantSettingUpdatableFieldsEquals(
            partialUpdatedBackupTenantSetting,
            getPersistedBackupTenantSetting(partialUpdatedBackupTenantSetting)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBackupTenantSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenantSetting.setId(longCount.incrementAndGet());

        // Create the BackupTenantSetting
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupTenantSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupTenantSettingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupTenantSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBackupTenantSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenantSetting.setId(longCount.incrementAndGet());

        // Create the BackupTenantSetting
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupTenantSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupTenantSettingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupTenantSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBackupTenantSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenantSetting.setId(longCount.incrementAndGet());

        // Create the BackupTenantSetting
        BackupTenantSettingDTO backupTenantSettingDTO = backupTenantSettingMapper.toDto(backupTenantSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupTenantSettingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(backupTenantSettingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupTenantSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBackupTenantSetting() throws Exception {
        // Initialize the database
        backupTenantSettingRepository.saveAndFlush(backupTenantSetting);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the backupTenantSetting
        restBackupTenantSettingMockMvc
            .perform(delete(ENTITY_API_URL_ID, backupTenantSetting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return backupTenantSettingRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected BackupTenantSetting getPersistedBackupTenantSetting(BackupTenantSetting backupTenantSetting) {
        return backupTenantSettingRepository.findById(backupTenantSetting.getId()).orElseThrow();
    }

    protected void assertPersistedBackupTenantSettingToMatchAllProperties(BackupTenantSetting expectedBackupTenantSetting) {
        assertBackupTenantSettingAllPropertiesEquals(
            expectedBackupTenantSetting,
            getPersistedBackupTenantSetting(expectedBackupTenantSetting)
        );
    }

    protected void assertPersistedBackupTenantSettingToMatchUpdatableProperties(BackupTenantSetting expectedBackupTenantSetting) {
        assertBackupTenantSettingAllUpdatablePropertiesEquals(
            expectedBackupTenantSetting,
            getPersistedBackupTenantSetting(expectedBackupTenantSetting)
        );
    }
}
