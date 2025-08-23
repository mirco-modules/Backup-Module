package org.khasanof.backup.service.mapper;

import static org.khasanof.backup.domain.BackupFileAsserts.*;
import static org.khasanof.backup.domain.BackupFileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BackupFileMapperTest {

    private BackupFileMapper backupFileMapper;

    @BeforeEach
    void setUp() {
        backupFileMapper = new BackupFileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBackupFileSample1();
        var actual = backupFileMapper.toEntity(backupFileMapper.toDto(expected));
        assertBackupFileAllPropertiesEquals(expected, actual);
    }
}
