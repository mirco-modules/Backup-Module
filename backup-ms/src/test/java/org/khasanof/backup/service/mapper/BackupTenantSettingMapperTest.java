package org.khasanof.backup.service.mapper;

import static org.khasanof.backup.domain.BackupTenantSettingAsserts.*;
import static org.khasanof.backup.domain.BackupTenantSettingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BackupTenantSettingMapperTest {

    private BackupTenantSettingMapper backupTenantSettingMapper;

    @BeforeEach
    void setUp() {
        backupTenantSettingMapper = new BackupTenantSettingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBackupTenantSettingSample1();
        var actual = backupTenantSettingMapper.toEntity(backupTenantSettingMapper.toDto(expected));
        assertBackupTenantSettingAllPropertiesEquals(expected, actual);
    }
}
