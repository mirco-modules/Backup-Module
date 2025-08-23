package org.khasanof.backup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.backup.domain.BackupJobAsserts.*;
import static org.khasanof.backup.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khasanof.backup.IntegrationTest;
import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.domain.common.BackupJob;
import org.khasanof.backup.domain.common.enumeration.BackupStatus;
import org.khasanof.backup.repository.common.BackupJobRepository;
import org.khasanof.backup.service.dto.BackupJobDTO;
import org.khasanof.backup.service.mapper.BackupJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BackupJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BackupJobResourceIT {

    private static final Instant DEFAULT_STARTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FINISHED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISHED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BackupStatus DEFAULT_STATUS = BackupStatus.PENDING;
    private static final BackupStatus UPDATED_STATUS = BackupStatus.RUNNING;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/backup-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BackupJobRepository backupJobRepository;

    @Autowired
    private BackupJobMapper backupJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBackupJobMockMvc;

    private BackupJob backupJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupJob createEntity(EntityManager em) {
        BackupJob backupJob = new BackupJob()
            .startedAt(DEFAULT_STARTED_AT)
            .finishedAt(DEFAULT_FINISHED_AT)
            .status(DEFAULT_STATUS)
            .message(DEFAULT_MESSAGE);
        return backupJob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupJob createUpdatedEntity(EntityManager em) {
        BackupJob backupJob = new BackupJob()
            .startedAt(UPDATED_STARTED_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE);
        return backupJob;
    }

    @BeforeEach
    public void initTest() {
        backupJob = createEntity(em);
    }

    @Test
    @Transactional
    void createBackupJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BackupJob
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);
        var returnedBackupJobDTO = om.readValue(
            restBackupJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupJobDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BackupJobDTO.class
        );

        // Validate the BackupJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBackupJob = backupJobMapper.toEntity(returnedBackupJobDTO);
        assertBackupJobUpdatableFieldsEquals(returnedBackupJob, getPersistedBackupJob(returnedBackupJob));
    }

    @Test
    @Transactional
    void createBackupJobWithExistingId() throws Exception {
        // Create the BackupJob with an existing ID
        backupJob.setId(1L);
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBackupJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BackupJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupJob.setStartedAt(null);

        // Create the BackupJob, which fails.
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);

        restBackupJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupJob.setStatus(null);

        // Create the BackupJob, which fails.
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);

        restBackupJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBackupJobs() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList
        restBackupJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].finishedAt").value(hasItem(DEFAULT_FINISHED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }

    @Test
    @Transactional
    void getBackupJob() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get the backupJob
        restBackupJobMockMvc
            .perform(get(ENTITY_API_URL_ID, backupJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(backupJob.getId().intValue()))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.finishedAt").value(DEFAULT_FINISHED_AT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }

    @Test
    @Transactional
    void getBackupJobsByIdFiltering() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        Long id = backupJob.getId();

        defaultBackupJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBackupJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBackupJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBackupJobsByStartedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where startedAt equals to
        defaultBackupJobFiltering("startedAt.equals=" + DEFAULT_STARTED_AT, "startedAt.equals=" + UPDATED_STARTED_AT);
    }

    @Test
    @Transactional
    void getAllBackupJobsByStartedAtIsInShouldWork() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where startedAt in
        defaultBackupJobFiltering("startedAt.in=" + DEFAULT_STARTED_AT + "," + UPDATED_STARTED_AT, "startedAt.in=" + UPDATED_STARTED_AT);
    }

    @Test
    @Transactional
    void getAllBackupJobsByStartedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where startedAt is not null
        defaultBackupJobFiltering("startedAt.specified=true", "startedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupJobsByFinishedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where finishedAt equals to
        defaultBackupJobFiltering("finishedAt.equals=" + DEFAULT_FINISHED_AT, "finishedAt.equals=" + UPDATED_FINISHED_AT);
    }

    @Test
    @Transactional
    void getAllBackupJobsByFinishedAtIsInShouldWork() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where finishedAt in
        defaultBackupJobFiltering(
            "finishedAt.in=" + DEFAULT_FINISHED_AT + "," + UPDATED_FINISHED_AT,
            "finishedAt.in=" + UPDATED_FINISHED_AT
        );
    }

    @Test
    @Transactional
    void getAllBackupJobsByFinishedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where finishedAt is not null
        defaultBackupJobFiltering("finishedAt.specified=true", "finishedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where status equals to
        defaultBackupJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBackupJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where status in
        defaultBackupJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBackupJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where status is not null
        defaultBackupJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupJobsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where message equals to
        defaultBackupJobFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllBackupJobsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where message in
        defaultBackupJobFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllBackupJobsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where message is not null
        defaultBackupJobFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupJobsByMessageContainsSomething() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where message contains
        defaultBackupJobFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllBackupJobsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        // Get all the backupJobList where message does not contain
        defaultBackupJobFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllBackupJobsByBackupFileIsEqualToSomething() throws Exception {
        BackupFile backupFile;
        if (TestUtil.findAll(em, BackupFile.class).isEmpty()) {
            backupJobRepository.saveAndFlush(backupJob);
            backupFile = BackupFileResourceIT.createEntity(em);
        } else {
            backupFile = TestUtil.findAll(em, BackupFile.class).get(0);
        }
        em.persist(backupFile);
        em.flush();
        backupJob.setBackupFile(backupFile);
        backupJobRepository.saveAndFlush(backupJob);
        Long backupFileId = backupFile.getId();
        // Get all the backupJobList where backupFile equals to backupFileId
        defaultBackupJobShouldBeFound("backupFileId.equals=" + backupFileId);

        // Get all the backupJobList where backupFile equals to (backupFileId + 1)
        defaultBackupJobShouldNotBeFound("backupFileId.equals=" + (backupFileId + 1));
    }

    private void defaultBackupJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBackupJobShouldBeFound(shouldBeFound);
        defaultBackupJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBackupJobShouldBeFound(String filter) throws Exception {
        restBackupJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].finishedAt").value(hasItem(DEFAULT_FINISHED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));

        // Check, that the count call also returns 1
        restBackupJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBackupJobShouldNotBeFound(String filter) throws Exception {
        restBackupJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBackupJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBackupJob() throws Exception {
        // Get the backupJob
        restBackupJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBackupJob() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupJob
        BackupJob updatedBackupJob = backupJobRepository.findById(backupJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBackupJob are not directly saved in db
        em.detach(updatedBackupJob);
        updatedBackupJob.startedAt(UPDATED_STARTED_AT).finishedAt(UPDATED_FINISHED_AT).status(UPDATED_STATUS).message(UPDATED_MESSAGE);
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(updatedBackupJob);

        restBackupJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the BackupJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBackupJobToMatchAllProperties(updatedBackupJob);
    }

    @Test
    @Transactional
    void putNonExistingBackupJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupJob.setId(longCount.incrementAndGet());

        // Create the BackupJob
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBackupJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupJob.setId(longCount.incrementAndGet());

        // Create the BackupJob
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBackupJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupJob.setId(longCount.incrementAndGet());

        // Create the BackupJob
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBackupJobWithPatch() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupJob using partial update
        BackupJob partialUpdatedBackupJob = new BackupJob();
        partialUpdatedBackupJob.setId(backupJob.getId());

        partialUpdatedBackupJob.startedAt(UPDATED_STARTED_AT).status(UPDATED_STATUS);

        restBackupJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBackupJob))
            )
            .andExpect(status().isOk());

        // Validate the BackupJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBackupJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBackupJob, backupJob),
            getPersistedBackupJob(backupJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateBackupJobWithPatch() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupJob using partial update
        BackupJob partialUpdatedBackupJob = new BackupJob();
        partialUpdatedBackupJob.setId(backupJob.getId());

        partialUpdatedBackupJob
            .startedAt(UPDATED_STARTED_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE);

        restBackupJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBackupJob))
            )
            .andExpect(status().isOk());

        // Validate the BackupJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBackupJobUpdatableFieldsEquals(partialUpdatedBackupJob, getPersistedBackupJob(partialUpdatedBackupJob));
    }

    @Test
    @Transactional
    void patchNonExistingBackupJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupJob.setId(longCount.incrementAndGet());

        // Create the BackupJob
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBackupJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupJob.setId(longCount.incrementAndGet());

        // Create the BackupJob
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBackupJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupJob.setId(longCount.incrementAndGet());

        // Create the BackupJob
        BackupJobDTO backupJobDTO = backupJobMapper.toDto(backupJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(backupJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBackupJob() throws Exception {
        // Initialize the database
        backupJobRepository.saveAndFlush(backupJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the backupJob
        restBackupJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, backupJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return backupJobRepository.count();
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

    protected BackupJob getPersistedBackupJob(BackupJob backupJob) {
        return backupJobRepository.findById(backupJob.getId()).orElseThrow();
    }

    protected void assertPersistedBackupJobToMatchAllProperties(BackupJob expectedBackupJob) {
        assertBackupJobAllPropertiesEquals(expectedBackupJob, getPersistedBackupJob(expectedBackupJob));
    }

    protected void assertPersistedBackupJobToMatchUpdatableProperties(BackupJob expectedBackupJob) {
        assertBackupJobAllUpdatablePropertiesEquals(expectedBackupJob, getPersistedBackupJob(expectedBackupJob));
    }
}
