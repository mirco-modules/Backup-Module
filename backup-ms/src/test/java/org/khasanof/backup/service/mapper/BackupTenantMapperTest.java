package org.khasanof.backup.service.mapper;

import static org.khasanof.backup.domain.BackupTenantAsserts.*;
import static org.khasanof.backup.domain.BackupTenantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BackupTenantMapperTest {

    private BackupTenantMapper backupTenantMapper;

    @BeforeEach
    void setUp() {
        backupTenantMapper = new BackupTenantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBackupTenantSample1();
        var actual = backupTenantMapper.toEntity(backupTenantMapper.toDto(expected));
        assertBackupTenantAllPropertiesEquals(expected, actual);
    }
}
