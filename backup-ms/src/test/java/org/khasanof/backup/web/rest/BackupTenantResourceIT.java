package org.khasanof.backup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.khasanof.backup.domain.BackupTenantAsserts.*;
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
import org.khasanof.backup.domain.BackupTenant;
import org.khasanof.backup.domain.BackupTenantSetting;
import org.khasanof.backup.repository.BackupTenantRepository;
import org.khasanof.backup.service.dto.BackupTenantDTO;
import org.khasanof.backup.service.mapper.BackupTenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BackupTenantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BackupTenantResourceIT {

    private static final String DEFAULT_TENANT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_DB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DB_HOST = "AAAAAAAAAA";
    private static final String UPDATED_DB_HOST = "BBBBBBBBBB";

    private static final Integer DEFAULT_DB_PORT = 1;
    private static final Integer UPDATED_DB_PORT = 2;
    private static final Integer SMALLER_DB_PORT = 1 - 1;

    private static final String DEFAULT_DB_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_DB_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_DB_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_DB_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/backup-tenants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BackupTenantRepository backupTenantRepository;

    @Autowired
    private BackupTenantMapper backupTenantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBackupTenantMockMvc;

    private BackupTenant backupTenant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupTenant createEntity(EntityManager em) {
        BackupTenant backupTenant = new BackupTenant()
            .tenantKey(DEFAULT_TENANT_KEY)
            .dbName(DEFAULT_DB_NAME)
            .dbHost(DEFAULT_DB_HOST)
            .dbPort(DEFAULT_DB_PORT)
            .dbUsername(DEFAULT_DB_USERNAME)
            .dbPassword(DEFAULT_DB_PASSWORD);
        return backupTenant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BackupTenant createUpdatedEntity(EntityManager em) {
        BackupTenant backupTenant = new BackupTenant()
            .tenantKey(UPDATED_TENANT_KEY)
            .dbName(UPDATED_DB_NAME)
            .dbHost(UPDATED_DB_HOST)
            .dbPort(UPDATED_DB_PORT)
            .dbUsername(UPDATED_DB_USERNAME)
            .dbPassword(UPDATED_DB_PASSWORD);
        return backupTenant;
    }

    @BeforeEach
    public void initTest() {
        backupTenant = createEntity(em);
    }

    @Test
    @Transactional
    void createBackupTenant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BackupTenant
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);
        var returnedBackupTenantDTO = om.readValue(
            restBackupTenantMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BackupTenantDTO.class
        );

        // Validate the BackupTenant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBackupTenant = backupTenantMapper.toEntity(returnedBackupTenantDTO);
        assertBackupTenantUpdatableFieldsEquals(returnedBackupTenant, getPersistedBackupTenant(returnedBackupTenant));
    }

    @Test
    @Transactional
    void createBackupTenantWithExistingId() throws Exception {
        // Create the BackupTenant with an existing ID
        backupTenant.setId(1L);
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBackupTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BackupTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenantKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupTenant.setTenantKey(null);

        // Create the BackupTenant, which fails.
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        restBackupTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDbNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupTenant.setDbName(null);

        // Create the BackupTenant, which fails.
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        restBackupTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDbHostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupTenant.setDbHost(null);

        // Create the BackupTenant, which fails.
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        restBackupTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDbPortIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupTenant.setDbPort(null);

        // Create the BackupTenant, which fails.
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        restBackupTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDbUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupTenant.setDbUsername(null);

        // Create the BackupTenant, which fails.
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        restBackupTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDbPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        backupTenant.setDbPassword(null);

        // Create the BackupTenant, which fails.
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        restBackupTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBackupTenants() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList
        restBackupTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupTenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantKey").value(hasItem(DEFAULT_TENANT_KEY)))
            .andExpect(jsonPath("$.[*].dbName").value(hasItem(DEFAULT_DB_NAME)))
            .andExpect(jsonPath("$.[*].dbHost").value(hasItem(DEFAULT_DB_HOST)))
            .andExpect(jsonPath("$.[*].dbPort").value(hasItem(DEFAULT_DB_PORT)))
            .andExpect(jsonPath("$.[*].dbUsername").value(hasItem(DEFAULT_DB_USERNAME)))
            .andExpect(jsonPath("$.[*].dbPassword").value(hasItem(DEFAULT_DB_PASSWORD)));
    }

    @Test
    @Transactional
    void getBackupTenant() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get the backupTenant
        restBackupTenantMockMvc
            .perform(get(ENTITY_API_URL_ID, backupTenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(backupTenant.getId().intValue()))
            .andExpect(jsonPath("$.tenantKey").value(DEFAULT_TENANT_KEY))
            .andExpect(jsonPath("$.dbName").value(DEFAULT_DB_NAME))
            .andExpect(jsonPath("$.dbHost").value(DEFAULT_DB_HOST))
            .andExpect(jsonPath("$.dbPort").value(DEFAULT_DB_PORT))
            .andExpect(jsonPath("$.dbUsername").value(DEFAULT_DB_USERNAME))
            .andExpect(jsonPath("$.dbPassword").value(DEFAULT_DB_PASSWORD));
    }

    @Test
    @Transactional
    void getBackupTenantsByIdFiltering() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        Long id = backupTenant.getId();

        defaultBackupTenantFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBackupTenantFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBackupTenantFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByTenantKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where tenantKey equals to
        defaultBackupTenantFiltering("tenantKey.equals=" + DEFAULT_TENANT_KEY, "tenantKey.equals=" + UPDATED_TENANT_KEY);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByTenantKeyIsInShouldWork() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where tenantKey in
        defaultBackupTenantFiltering("tenantKey.in=" + DEFAULT_TENANT_KEY + "," + UPDATED_TENANT_KEY, "tenantKey.in=" + UPDATED_TENANT_KEY);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByTenantKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where tenantKey is not null
        defaultBackupTenantFiltering("tenantKey.specified=true", "tenantKey.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupTenantsByTenantKeyContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where tenantKey contains
        defaultBackupTenantFiltering("tenantKey.contains=" + DEFAULT_TENANT_KEY, "tenantKey.contains=" + UPDATED_TENANT_KEY);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByTenantKeyNotContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where tenantKey does not contain
        defaultBackupTenantFiltering("tenantKey.doesNotContain=" + UPDATED_TENANT_KEY, "tenantKey.doesNotContain=" + DEFAULT_TENANT_KEY);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbNameIsEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbName equals to
        defaultBackupTenantFiltering("dbName.equals=" + DEFAULT_DB_NAME, "dbName.equals=" + UPDATED_DB_NAME);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbNameIsInShouldWork() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbName in
        defaultBackupTenantFiltering("dbName.in=" + DEFAULT_DB_NAME + "," + UPDATED_DB_NAME, "dbName.in=" + UPDATED_DB_NAME);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbName is not null
        defaultBackupTenantFiltering("dbName.specified=true", "dbName.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbNameContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbName contains
        defaultBackupTenantFiltering("dbName.contains=" + DEFAULT_DB_NAME, "dbName.contains=" + UPDATED_DB_NAME);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbNameNotContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbName does not contain
        defaultBackupTenantFiltering("dbName.doesNotContain=" + UPDATED_DB_NAME, "dbName.doesNotContain=" + DEFAULT_DB_NAME);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbHostIsEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbHost equals to
        defaultBackupTenantFiltering("dbHost.equals=" + DEFAULT_DB_HOST, "dbHost.equals=" + UPDATED_DB_HOST);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbHostIsInShouldWork() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbHost in
        defaultBackupTenantFiltering("dbHost.in=" + DEFAULT_DB_HOST + "," + UPDATED_DB_HOST, "dbHost.in=" + UPDATED_DB_HOST);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbHostIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbHost is not null
        defaultBackupTenantFiltering("dbHost.specified=true", "dbHost.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbHostContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbHost contains
        defaultBackupTenantFiltering("dbHost.contains=" + DEFAULT_DB_HOST, "dbHost.contains=" + UPDATED_DB_HOST);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbHostNotContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbHost does not contain
        defaultBackupTenantFiltering("dbHost.doesNotContain=" + UPDATED_DB_HOST, "dbHost.doesNotContain=" + DEFAULT_DB_HOST);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPortIsEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPort equals to
        defaultBackupTenantFiltering("dbPort.equals=" + DEFAULT_DB_PORT, "dbPort.equals=" + UPDATED_DB_PORT);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPortIsInShouldWork() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPort in
        defaultBackupTenantFiltering("dbPort.in=" + DEFAULT_DB_PORT + "," + UPDATED_DB_PORT, "dbPort.in=" + UPDATED_DB_PORT);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPort is not null
        defaultBackupTenantFiltering("dbPort.specified=true", "dbPort.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPort is greater than or equal to
        defaultBackupTenantFiltering("dbPort.greaterThanOrEqual=" + DEFAULT_DB_PORT, "dbPort.greaterThanOrEqual=" + UPDATED_DB_PORT);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPortIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPort is less than or equal to
        defaultBackupTenantFiltering("dbPort.lessThanOrEqual=" + DEFAULT_DB_PORT, "dbPort.lessThanOrEqual=" + SMALLER_DB_PORT);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPortIsLessThanSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPort is less than
        defaultBackupTenantFiltering("dbPort.lessThan=" + UPDATED_DB_PORT, "dbPort.lessThan=" + DEFAULT_DB_PORT);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPortIsGreaterThanSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPort is greater than
        defaultBackupTenantFiltering("dbPort.greaterThan=" + SMALLER_DB_PORT, "dbPort.greaterThan=" + DEFAULT_DB_PORT);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbUsername equals to
        defaultBackupTenantFiltering("dbUsername.equals=" + DEFAULT_DB_USERNAME, "dbUsername.equals=" + UPDATED_DB_USERNAME);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbUsername in
        defaultBackupTenantFiltering(
            "dbUsername.in=" + DEFAULT_DB_USERNAME + "," + UPDATED_DB_USERNAME,
            "dbUsername.in=" + UPDATED_DB_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbUsername is not null
        defaultBackupTenantFiltering("dbUsername.specified=true", "dbUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbUsernameContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbUsername contains
        defaultBackupTenantFiltering("dbUsername.contains=" + DEFAULT_DB_USERNAME, "dbUsername.contains=" + UPDATED_DB_USERNAME);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbUsername does not contain
        defaultBackupTenantFiltering(
            "dbUsername.doesNotContain=" + UPDATED_DB_USERNAME,
            "dbUsername.doesNotContain=" + DEFAULT_DB_USERNAME
        );
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPassword equals to
        defaultBackupTenantFiltering("dbPassword.equals=" + DEFAULT_DB_PASSWORD, "dbPassword.equals=" + UPDATED_DB_PASSWORD);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPassword in
        defaultBackupTenantFiltering(
            "dbPassword.in=" + DEFAULT_DB_PASSWORD + "," + UPDATED_DB_PASSWORD,
            "dbPassword.in=" + UPDATED_DB_PASSWORD
        );
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPassword is not null
        defaultBackupTenantFiltering("dbPassword.specified=true", "dbPassword.specified=false");
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPasswordContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPassword contains
        defaultBackupTenantFiltering("dbPassword.contains=" + DEFAULT_DB_PASSWORD, "dbPassword.contains=" + UPDATED_DB_PASSWORD);
    }

    @Test
    @Transactional
    void getAllBackupTenantsByDbPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        // Get all the backupTenantList where dbPassword does not contain
        defaultBackupTenantFiltering(
            "dbPassword.doesNotContain=" + UPDATED_DB_PASSWORD,
            "dbPassword.doesNotContain=" + DEFAULT_DB_PASSWORD
        );
    }

    @Test
    @Transactional
    void getAllBackupTenantsBySettingIsEqualToSomething() throws Exception {
        BackupTenantSetting setting;
        if (TestUtil.findAll(em, BackupTenantSetting.class).isEmpty()) {
            backupTenantRepository.saveAndFlush(backupTenant);
            setting = BackupTenantSettingResourceIT.createEntity(em);
        } else {
            setting = TestUtil.findAll(em, BackupTenantSetting.class).get(0);
        }
        em.persist(setting);
        em.flush();
        backupTenant.setSetting(setting);
        backupTenantRepository.saveAndFlush(backupTenant);
        Long settingId = setting.getId();
        // Get all the backupTenantList where setting equals to settingId
        defaultBackupTenantShouldBeFound("settingId.equals=" + settingId);

        // Get all the backupTenantList where setting equals to (settingId + 1)
        defaultBackupTenantShouldNotBeFound("settingId.equals=" + (settingId + 1));
    }

    private void defaultBackupTenantFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBackupTenantShouldBeFound(shouldBeFound);
        defaultBackupTenantShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBackupTenantShouldBeFound(String filter) throws Exception {
        restBackupTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(backupTenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantKey").value(hasItem(DEFAULT_TENANT_KEY)))
            .andExpect(jsonPath("$.[*].dbName").value(hasItem(DEFAULT_DB_NAME)))
            .andExpect(jsonPath("$.[*].dbHost").value(hasItem(DEFAULT_DB_HOST)))
            .andExpect(jsonPath("$.[*].dbPort").value(hasItem(DEFAULT_DB_PORT)))
            .andExpect(jsonPath("$.[*].dbUsername").value(hasItem(DEFAULT_DB_USERNAME)))
            .andExpect(jsonPath("$.[*].dbPassword").value(hasItem(DEFAULT_DB_PASSWORD)));

        // Check, that the count call also returns 1
        restBackupTenantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBackupTenantShouldNotBeFound(String filter) throws Exception {
        restBackupTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBackupTenantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBackupTenant() throws Exception {
        // Get the backupTenant
        restBackupTenantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBackupTenant() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupTenant
        BackupTenant updatedBackupTenant = backupTenantRepository.findById(backupTenant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBackupTenant are not directly saved in db
        em.detach(updatedBackupTenant);
        updatedBackupTenant
            .tenantKey(UPDATED_TENANT_KEY)
            .dbName(UPDATED_DB_NAME)
            .dbHost(UPDATED_DB_HOST)
            .dbPort(UPDATED_DB_PORT)
            .dbUsername(UPDATED_DB_USERNAME)
            .dbPassword(UPDATED_DB_PASSWORD);
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(updatedBackupTenant);

        restBackupTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, backupTenantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupTenantDTO))
            )
            .andExpect(status().isOk());

        // Validate the BackupTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBackupTenantToMatchAllProperties(updatedBackupTenant);
    }

    @Test
    @Transactional
    void putNonExistingBackupTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenant.setId(longCount.incrementAndGet());

        // Create the BackupTenant
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, backupTenantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupTenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBackupTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenant.setId(longCount.incrementAndGet());

        // Create the BackupTenant
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(backupTenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBackupTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenant.setId(longCount.incrementAndGet());

        // Create the BackupTenant
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupTenantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(backupTenantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBackupTenantWithPatch() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupTenant using partial update
        BackupTenant partialUpdatedBackupTenant = new BackupTenant();
        partialUpdatedBackupTenant.setId(backupTenant.getId());

        partialUpdatedBackupTenant.dbPort(UPDATED_DB_PORT).dbPassword(UPDATED_DB_PASSWORD);

        restBackupTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBackupTenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBackupTenant))
            )
            .andExpect(status().isOk());

        // Validate the BackupTenant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBackupTenantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBackupTenant, backupTenant),
            getPersistedBackupTenant(backupTenant)
        );
    }

    @Test
    @Transactional
    void fullUpdateBackupTenantWithPatch() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the backupTenant using partial update
        BackupTenant partialUpdatedBackupTenant = new BackupTenant();
        partialUpdatedBackupTenant.setId(backupTenant.getId());

        partialUpdatedBackupTenant
            .tenantKey(UPDATED_TENANT_KEY)
            .dbName(UPDATED_DB_NAME)
            .dbHost(UPDATED_DB_HOST)
            .dbPort(UPDATED_DB_PORT)
            .dbUsername(UPDATED_DB_USERNAME)
            .dbPassword(UPDATED_DB_PASSWORD);

        restBackupTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBackupTenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBackupTenant))
            )
            .andExpect(status().isOk());

        // Validate the BackupTenant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBackupTenantUpdatableFieldsEquals(partialUpdatedBackupTenant, getPersistedBackupTenant(partialUpdatedBackupTenant));
    }

    @Test
    @Transactional
    void patchNonExistingBackupTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenant.setId(longCount.incrementAndGet());

        // Create the BackupTenant
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBackupTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, backupTenantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupTenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBackupTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenant.setId(longCount.incrementAndGet());

        // Create the BackupTenant
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(backupTenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BackupTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBackupTenant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        backupTenant.setId(longCount.incrementAndGet());

        // Create the BackupTenant
        BackupTenantDTO backupTenantDTO = backupTenantMapper.toDto(backupTenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBackupTenantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(backupTenantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BackupTenant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBackupTenant() throws Exception {
        // Initialize the database
        backupTenantRepository.saveAndFlush(backupTenant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the backupTenant
        restBackupTenantMockMvc
            .perform(delete(ENTITY_API_URL_ID, backupTenant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return backupTenantRepository.count();
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

    protected BackupTenant getPersistedBackupTenant(BackupTenant backupTenant) {
        return backupTenantRepository.findById(backupTenant.getId()).orElseThrow();
    }

    protected void assertPersistedBackupTenantToMatchAllProperties(BackupTenant expectedBackupTenant) {
        assertBackupTenantAllPropertiesEquals(expectedBackupTenant, getPersistedBackupTenant(expectedBackupTenant));
    }

    protected void assertPersistedBackupTenantToMatchUpdatableProperties(BackupTenant expectedBackupTenant) {
        assertBackupTenantAllUpdatablePropertiesEquals(expectedBackupTenant, getPersistedBackupTenant(expectedBackupTenant));
    }
}
