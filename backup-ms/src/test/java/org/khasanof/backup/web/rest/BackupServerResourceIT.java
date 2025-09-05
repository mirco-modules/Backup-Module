package org.khasanof.backup.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khasanof.backup.IntegrationTest;
import org.khasanof.backup.domain.common.BackupServer;
import org.khasanof.backup.domain.common.enumeration.BackupServerStatus;
import org.khasanof.backup.repository.common.BackupServerRepository;
import org.khasanof.backup.service.dto.BackupServerDTO;
import org.khasanof.backup.service.mapper.BackupServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.backup.domain.BackupServerAsserts.*;
import static org.khasanof.backup.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BackupServerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BackupServerResourceIT {

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;
    private static final Integer SMALLER_PORT = 1 - 1;

    private static final BackupServerStatus DEFAULT_STATUS = BackupServerStatus.UP;
    private static final BackupServerStatus UPDATED_STATUS = BackupServerStatus.DOWN;

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/backup-servers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BackupServerRepository backupServerRepository;

    @Autowired
    private BackupServerMapper backupServerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBackupServerMockMvc;

    private BackupServer backupServer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupServer createEntity(EntityManager em) {
        BackupServer backupServer = new BackupServer()
            .port(DEFAULT_PORT)
            .status(DEFAULT_STATUS)
            .host(DEFAULT_HOST)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD);
        return backupServer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupServer createUpdatedEntity(EntityManager em) {
        BackupServer backupServer = new BackupServer()
            .port(UPDATED_PORT)
            .status(UPDATED_STATUS)
            .host(UPDATED_HOST)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);
        return backupServer;
    }

    @BeforeEach
    public void initTest() {
        backupServer = createEntity(em);
    }

    @Test
    @Transactional
    void createBackupServer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BackupServer
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);
        var returnedBackupServerDTO = om.readValue(
            restBackupServerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupServerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BackupServerDTO.class
        );

        // Validate the BackupServer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBackupServer = backupServerMapper.toEntity(returnedBackupServerDTO);
        assertBackupServerUpdatableFieldsEquals(returnedBackupServer, getPersistedBackupServer(returnedBackupServer));
    }

    @Test
    @Transactional
    void createBackupServerWithExistingId() throws Exception {
        // Create the BackupServer with an existing ID
        backupServer.setId(1L);
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBackupServerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupServerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BackupServer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPortIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupServer.setPort(null);

        // Create the BackupServer, which fails.
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        restBackupServerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupServerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupServer.setStatus(null);

        // Create the BackupServer, which fails.
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        restBackupServerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupServerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupServer.setHost(null);

        // Create the BackupServer, which fails.
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        restBackupServerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupServerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupServer.setUsername(null);

        // Create the BackupServer, which fails.
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        restBackupServerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupServerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupServer.setPassword(null);

        // Create the BackupServer, which fails.
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        restBackupServerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupServerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBackupServers() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList
        restBackupServerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupServer.getId().intValue())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getBackupServer() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get the backupServer
        restBackupServerMockMvc
            .perform(get(ENTITY_API_URL_ID, backupServer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(backupServer.getId().intValue()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getBackupServersByIdFiltering() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        Long id = backupServer.getId();

        defaultBackupServerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBackupServerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBackupServerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBackupServersByPortIsEqualToSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where port equals to
        defaultBackupServerFiltering("port.equals=" + DEFAULT_PORT, "port.equals=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    void getAllBackupServersByPortIsInShouldWork() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where port in
        defaultBackupServerFiltering("port.in=" + DEFAULT_PORT + "," + UPDATED_PORT, "port.in=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    void getAllBackupServersByPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where port is not null
        defaultBackupServerFiltering("port.specified=true", "port.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupServersByPortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where port is greater than or equal to
        defaultBackupServerFiltering("port.greaterThanOrEqual=" + DEFAULT_PORT, "port.greaterThanOrEqual=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    void getAllBackupServersByPortIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where port is less than or equal to
        defaultBackupServerFiltering("port.lessThanOrEqual=" + DEFAULT_PORT, "port.lessThanOrEqual=" + SMALLER_PORT);
    }

    @Test
    @Transactional
    void getAllBackupServersByPortIsLessThanSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where port is less than
        defaultBackupServerFiltering("port.lessThan=" + UPDATED_PORT, "port.lessThan=" + DEFAULT_PORT);
    }

    @Test
    @Transactional
    void getAllBackupServersByPortIsGreaterThanSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where port is greater than
        defaultBackupServerFiltering("port.greaterThan=" + SMALLER_PORT, "port.greaterThan=" + DEFAULT_PORT);
    }

    @Test
    @Transactional
    void getAllBackupServersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where status equals to
        defaultBackupServerFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBackupServersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where status in
        defaultBackupServerFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBackupServersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where status is not null
        defaultBackupServerFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupServersByHostIsEqualToSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where host equals to
        defaultBackupServerFiltering("host.equals=" + DEFAULT_HOST, "host.equals=" + UPDATED_HOST);
    }

    @Test
    @Transactional
    void getAllBackupServersByHostIsInShouldWork() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where host in
        defaultBackupServerFiltering("host.in=" + DEFAULT_HOST + "," + UPDATED_HOST, "host.in=" + UPDATED_HOST);
    }

    @Test
    @Transactional
    void getAllBackupServersByHostIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where host is not null
        defaultBackupServerFiltering("host.specified=true", "host.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupServersByHostContainsSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where host contains
        defaultBackupServerFiltering("host.contains=" + DEFAULT_HOST, "host.contains=" + UPDATED_HOST);
    }

    @Test
    @Transactional
    void getAllBackupServersByHostNotContainsSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where host does not contain
        defaultBackupServerFiltering("host.doesNotContain=" + UPDATED_HOST, "host.doesNotContain=" + DEFAULT_HOST);
    }

    @Test
    @Transactional
    void getAllBackupServersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where username equals to
        defaultBackupServerFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllBackupServersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where username in
        defaultBackupServerFiltering("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME, "username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllBackupServersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where username is not null
        defaultBackupServerFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupServersByUsernameContainsSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where username contains
        defaultBackupServerFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllBackupServersByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where username does not contain
        defaultBackupServerFiltering("username.doesNotContain=" + UPDATED_USERNAME, "username.doesNotContain=" + DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void getAllBackupServersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where password equals to
        defaultBackupServerFiltering("password.equals=" + DEFAULT_PASSWORD, "password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllBackupServersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where password in
        defaultBackupServerFiltering("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD, "password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllBackupServersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where password is not null
        defaultBackupServerFiltering("password.specified=true", "password.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupServersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where password contains
        defaultBackupServerFiltering("password.contains=" + DEFAULT_PASSWORD, "password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllBackupServersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        // Get all the backupServerList where password does not contain
        defaultBackupServerFiltering("password.doesNotContain=" + UPDATED_PASSWORD, "password.doesNotContain=" + DEFAULT_PASSWORD);
    }

    private void defaultBackupServerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBackupServerShouldBeFound(shouldBeFound);
        defaultBackupServerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBackupServerShouldBeFound(String filter) throws Exception {
        restBackupServerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupServer.getId().intValue())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));

        // Check, that the count call also returns 1
        restBackupServerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBackupServerShouldNotBeFound(String filter) throws Exception {
        restBackupServerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBackupServerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBackupServer() throws Exception {
        // Get the backupServer
        restBackupServerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBackupServer() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupServer
        BackupServer updatedBackupServer = backupServerRepository.findById(backupServer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBackupServer are not directly saved in db
        em.detach(updatedBackupServer);
        updatedBackupServer
            .port(UPDATED_PORT)
            .status(UPDATED_STATUS)
            .host(UPDATED_HOST)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(updatedBackupServer);

        restBackupServerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, backupServerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupServerDTO))
            )
            .andExpect(status().isOk());

        // Validate the BackupServer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBackupServerToMatchAllProperties(updatedBackupServer);
    }

    @Test
    @Transactional
    void putNonExistingBackupServer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupServer.setId(longCount.incrementAndGet());

        // Create the BackupServer
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupServerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, backupServerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupServerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupServer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBackupServer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupServer.setId(longCount.incrementAndGet());

        // Create the BackupServer
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupServerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupServerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupServer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBackupServer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupServer.setId(longCount.incrementAndGet());

        // Create the BackupServer
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupServerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupServerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupServer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBackupServerWithPatch() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupServer using partial update
        BackupServer partialUpdatedBackupServer = new BackupServer();
        partialUpdatedBackupServer.setId(backupServer.getId());

        partialUpdatedBackupServer.status(UPDATED_STATUS);

        restBackupServerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBackupServer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBackupServer))
            )
            .andExpect(status().isOk());

        // Validate the BackupServer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBackupServerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBackupServer, backupServer),
            getPersistedBackupServer(backupServer)
        );
    }

    @Test
    @Transactional
    void fullUpdateBackupServerWithPatch() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupServer using partial update
        BackupServer partialUpdatedBackupServer = new BackupServer();
        partialUpdatedBackupServer.setId(backupServer.getId());

        partialUpdatedBackupServer
            .port(UPDATED_PORT)
            .status(UPDATED_STATUS)
            .host(UPDATED_HOST)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);

        restBackupServerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBackupServer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBackupServer))
            )
            .andExpect(status().isOk());

        // Validate the BackupServer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBackupServerUpdatableFieldsEquals(partialUpdatedBackupServer, getPersistedBackupServer(partialUpdatedBackupServer));
    }

    @Test
    @Transactional
    void patchNonExistingBackupServer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupServer.setId(longCount.incrementAndGet());

        // Create the BackupServer
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupServerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, backupServerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupServerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupServer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBackupServer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupServer.setId(longCount.incrementAndGet());

        // Create the BackupServer
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupServerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupServerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupServer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBackupServer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupServer.setId(longCount.incrementAndGet());

        // Create the BackupServer
        BackupServerDTO backupServerDTO = backupServerMapper.toDto(backupServer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupServerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(backupServerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupServer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBackupServer() throws Exception {
        // Initialize the database
        backupServerRepository.saveAndFlush(backupServer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the backupServer
        restBackupServerMockMvc
            .perform(delete(ENTITY_API_URL_ID, backupServer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return backupServerRepository.count();
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

    protected BackupServer getPersistedBackupServer(BackupServer backupServer) {
        return backupServerRepository.findById(backupServer.getId()).orElseThrow();
    }

    protected void assertPersistedBackupServerToMatchAllProperties(BackupServer expectedBackupServer) {
        assertBackupServerAllPropertiesEquals(expectedBackupServer, getPersistedBackupServer(expectedBackupServer));
    }

    protected void assertPersistedBackupServerToMatchUpdatableProperties(BackupServer expectedBackupServer) {
        assertBackupServerAllUpdatablePropertiesEquals(expectedBackupServer, getPersistedBackupServer(expectedBackupServer));
    }
}
