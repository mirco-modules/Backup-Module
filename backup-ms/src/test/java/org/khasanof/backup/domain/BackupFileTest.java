package org.khasanof.backup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.backup.domain.BackupFileTestSamples.*;
import static org.khasanof.backup.domain.BackupJobTestSamples.*;
import static org.khasanof.backup.domain.BackupTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import org.khasanof.backup.web.rest.TestUtil;

class BackupFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupFile.class);
        BackupFile backupFile1 = getBackupFileSample1();
        BackupFile backupFile2 = new BackupFile();
        assertThat(backupFile1).isNotEqualTo(backupFile2);

        backupFile2.setId(backupFile1.getId());
        assertThat(backupFile1).isEqualTo(backupFile2);

        backupFile2 = getBackupFileSample2();
        assertThat(backupFile1).isNotEqualTo(backupFile2);
    }

    @Test
    void backupJobTest() throws Exception {
        BackupFile backupFile = getBackupFileRandomSampleGenerator();
        BackupJob backupJobBack = getBackupJobRandomSampleGenerator();

        backupFile.setBackupJob(backupJobBack);
        assertThat(backupFile.getBackupJob()).isEqualTo(backupJobBack);
        assertThat(backupJobBack.getBackupFile()).isEqualTo(backupFile);

        backupFile.backupJob(null);
        assertThat(backupFile.getBackupJob()).isNull();
        assertThat(backupJobBack.getBackupFile()).isNull();
    }

    @Test
    void tenantTest() throws Exception {
        BackupFile backupFile = getBackupFileRandomSampleGenerator();
        BackupTenant backupTenantBack = getBackupTenantRandomSampleGenerator();

        backupFile.setTenant(backupTenantBack);
        assertThat(backupFile.getTenant()).isEqualTo(backupTenantBack);

        backupFile.tenant(null);
        assertThat(backupFile.getTenant()).isNull();
    }
}
