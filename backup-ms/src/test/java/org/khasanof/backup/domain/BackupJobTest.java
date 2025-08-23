package org.khasanof.backup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.backup.domain.BackupFileTestSamples.*;
import static org.khasanof.backup.domain.BackupJobTestSamples.*;

import org.junit.jupiter.api.Test;
import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.domain.common.BackupJob;
import org.khasanof.backup.web.rest.TestUtil;

class BackupJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupJob.class);
        BackupJob backupJob1 = getBackupJobSample1();
        BackupJob backupJob2 = new BackupJob();
        assertThat(backupJob1).isNotEqualTo(backupJob2);

        backupJob2.setId(backupJob1.getId());
        assertThat(backupJob1).isEqualTo(backupJob2);

        backupJob2 = getBackupJobSample2();
        assertThat(backupJob1).isNotEqualTo(backupJob2);
    }

    @Test
    void backupFileTest() throws Exception {
        BackupJob backupJob = getBackupJobRandomSampleGenerator();
        BackupFile backupFileBack = getBackupFileRandomSampleGenerator();

        backupJob.setBackupFile(backupFileBack);
        assertThat(backupJob.getBackupFile()).isEqualTo(backupFileBack);

        backupJob.backupFile(null);
        assertThat(backupJob.getBackupFile()).isNull();
    }
}
