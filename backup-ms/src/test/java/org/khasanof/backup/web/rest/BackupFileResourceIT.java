package org.khasanof.backup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.backup.domain.BackupFileAsserts.*;
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
import org.khasanof.backup.domain.BackupFile;
import org.khasanof.backup.domain.BackupTenant;
import org.khasanof.backup.repository.BackupFileRepository;
import org.khasanof.backup.service.dto.BackupFileDTO;
import org.khasanof.backup.service.mapper.BackupFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BackupFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BackupFileResourceIT {

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;
    private static final Long SMALLER_FILE_SIZE = 1L - 1L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/backup-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BackupFileRepository backupFileRepository;

    @Autowired
    private BackupFileMapper backupFileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBackupFileMockMvc;

    private BackupFile backupFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupFile createEntity(EntityManager em) {
        BackupFile backupFile = new BackupFile().filePath(DEFAULT_FILE_PATH).fileSize(DEFAULT_FILE_SIZE).createdAt(DEFAULT_CREATED_AT);
        return backupFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupFile createUpdatedEntity(EntityManager em) {
        BackupFile backupFile = new BackupFile().filePath(UPDATED_FILE_PATH).fileSize(UPDATED_FILE_SIZE).createdAt(UPDATED_CREATED_AT);
        return backupFile;
    }

    @BeforeEach
    public void initTest() {
        backupFile = createEntity(em);
    }

    @Test
    @Transactional
    void createBackupFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BackupFile
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);
        var returnedBackupFileDTO = om.readValue(
            restBackupFileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupFileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BackupFileDTO.class
        );

        // Validate the BackupFile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBackupFile = backupFileMapper.toEntity(returnedBackupFileDTO);
        assertBackupFileUpdatableFieldsEquals(returnedBackupFile, getPersistedBackupFile(returnedBackupFile));
    }

    @Test
    @Transactional
    void createBackupFileWithExistingId() throws Exception {
        // Create the BackupFile with an existing ID
        backupFile.setId(1L);
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBackupFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BackupFile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFilePathIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupFile.setFilePath(null);

        // Create the BackupFile, which fails.
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        restBackupFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupFile.setFileSize(null);

        // Create the BackupFile, which fails.
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        restBackupFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupFile.setCreatedAt(null);

        // Create the BackupFile, which fails.
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        restBackupFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBackupFiles() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList
        restBackupFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getBackupFile() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get the backupFile
        restBackupFileMockMvc
            .perform(get(ENTITY_API_URL_ID, backupFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(backupFile.getId().intValue()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getBackupFilesByIdFiltering() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        Long id = backupFile.getId();

        defaultBackupFileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBackupFileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBackupFileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where filePath equals to
        defaultBackupFileFiltering("filePath.equals=" + DEFAULT_FILE_PATH, "filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where filePath in
        defaultBackupFileFiltering("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH, "filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where filePath is not null
        defaultBackupFileFiltering("filePath.specified=true", "filePath.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupFilesByFilePathContainsSomething() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where filePath contains
        defaultBackupFileFiltering("filePath.contains=" + DEFAULT_FILE_PATH, "filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where filePath does not contain
        defaultBackupFileFiltering("filePath.doesNotContain=" + UPDATED_FILE_PATH, "filePath.doesNotContain=" + DEFAULT_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where fileSize equals to
        defaultBackupFileFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where fileSize in
        defaultBackupFileFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where fileSize is not null
        defaultBackupFileFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupFilesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where fileSize is greater than or equal to
        defaultBackupFileFiltering("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where fileSize is less than or equal to
        defaultBackupFileFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where fileSize is less than
        defaultBackupFileFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllBackupFilesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where fileSize is greater than
        defaultBackupFileFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllBackupFilesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where createdAt equals to
        defaultBackupFileFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBackupFilesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where createdAt in
        defaultBackupFileFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBackupFilesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        // Get all the backupFileList where createdAt is not null
        defaultBackupFileFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupFilesByTenantIsEqualToSomething() throws Exception {
        BackupTenant tenant;
        if (TestUtil.findAll(em, BackupTenant.class).isEmpty()) {
            backupFileRepository.saveAndFlush(backupFile);
            tenant = BackupTenantResourceIT.createEntity(em);
        } else {
            tenant = TestUtil.findAll(em, BackupTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        backupFile.setTenant(tenant);
        backupFileRepository.saveAndFlush(backupFile);
        Long tenantId = tenant.getId();
        // Get all the backupFileList where tenant equals to tenantId
        defaultBackupFileShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the backupFileList where tenant equals to (tenantId + 1)
        defaultBackupFileShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultBackupFileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBackupFileShouldBeFound(shouldBeFound);
        defaultBackupFileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBackupFileShouldBeFound(String filter) throws Exception {
        restBackupFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restBackupFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBackupFileShouldNotBeFound(String filter) throws Exception {
        restBackupFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBackupFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBackupFile() throws Exception {
        // Get the backupFile
        restBackupFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBackupFile() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupFile
        BackupFile updatedBackupFile = backupFileRepository.findById(backupFile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBackupFile are not directly saved in db
        em.detach(updatedBackupFile);
        updatedBackupFile.filePath(UPDATED_FILE_PATH).fileSize(UPDATED_FILE_SIZE).createdAt(UPDATED_CREATED_AT);
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(updatedBackupFile);

        restBackupFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, backupFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the BackupFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBackupFileToMatchAllProperties(updatedBackupFile);
    }

    @Test
    @Transactional
    void putNonExistingBackupFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupFile.setId(longCount.incrementAndGet());

        // Create the BackupFile
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, backupFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBackupFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupFile.setId(longCount.incrementAndGet());

        // Create the BackupFile
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBackupFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupFile.setId(longCount.incrementAndGet());

        // Create the BackupFile
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBackupFileWithPatch() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupFile using partial update
        BackupFile partialUpdatedBackupFile = new BackupFile();
        partialUpdatedBackupFile.setId(backupFile.getId());

        partialUpdatedBackupFile.fileSize(UPDATED_FILE_SIZE).createdAt(UPDATED_CREATED_AT);

        restBackupFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBackupFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBackupFile))
            )
            .andExpect(status().isOk());

        // Validate the BackupFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBackupFileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBackupFile, backupFile),
            getPersistedBackupFile(backupFile)
        );
    }

    @Test
    @Transactional
    void fullUpdateBackupFileWithPatch() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupFile using partial update
        BackupFile partialUpdatedBackupFile = new BackupFile();
        partialUpdatedBackupFile.setId(backupFile.getId());

        partialUpdatedBackupFile.filePath(UPDATED_FILE_PATH).fileSize(UPDATED_FILE_SIZE).createdAt(UPDATED_CREATED_AT);

        restBackupFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBackupFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBackupFile))
            )
            .andExpect(status().isOk());

        // Validate the BackupFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBackupFileUpdatableFieldsEquals(partialUpdatedBackupFile, getPersistedBackupFile(partialUpdatedBackupFile));
    }

    @Test
    @Transactional
    void patchNonExistingBackupFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupFile.setId(longCount.incrementAndGet());

        // Create the BackupFile
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, backupFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBackupFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupFile.setId(longCount.incrementAndGet());

        // Create the BackupFile
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBackupFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupFile.setId(longCount.incrementAndGet());

        // Create the BackupFile
        BackupFileDTO backupFileDTO = backupFileMapper.toDto(backupFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(backupFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBackupFile() throws Exception {
        // Initialize the database
        backupFileRepository.saveAndFlush(backupFile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the backupFile
        restBackupFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, backupFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return backupFileRepository.count();
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

    protected BackupFile getPersistedBackupFile(BackupFile backupFile) {
        return backupFileRepository.findById(backupFile.getId()).orElseThrow();
    }

    protected void assertPersistedBackupFileToMatchAllProperties(BackupFile expectedBackupFile) {
        assertBackupFileAllPropertiesEquals(expectedBackupFile, getPersistedBackupFile(expectedBackupFile));
    }

    protected void assertPersistedBackupFileToMatchUpdatableProperties(BackupFile expectedBackupFile) {
        assertBackupFileAllUpdatablePropertiesEquals(expectedBackupFile, getPersistedBackupFile(expectedBackupFile));
    }
}
