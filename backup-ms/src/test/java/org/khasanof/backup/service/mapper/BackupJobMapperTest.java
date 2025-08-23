package org.khasanof.backup.service.mapper;

import static org.khasanof.backup.domain.BackupJobAsserts.*;
import static org.khasanof.backup.domain.BackupJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BackupJobMapperTest {

    private BackupJobMapper backupJobMapper;

    @BeforeEach
    void setUp() {
        backupJobMapper = new BackupJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBackupJobSample1();
        var actual = backupJobMapper.toEntity(backupJobMapper.toDto(expected));
        assertBackupJobAllPropertiesEquals(expected, actual);
    }
}
