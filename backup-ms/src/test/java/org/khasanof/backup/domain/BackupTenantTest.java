package org.khasanof.backup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.backup.domain.BackupFileTestSamples.*;
import static org.khasanof.backup.domain.BackupTenantSettingTestSamples.*;
import static org.khasanof.backup.domain.BackupTenantTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.web.rest.TestUtil;

class BackupTenantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupTenant.class);
        BackupTenant backupTenant1 = getBackupTenantSample1();
        BackupTenant backupTenant2 = new BackupTenant();
        assertThat(backupTenant1).isNotEqualTo(backupTenant2);

        backupTenant2.setId(backupTenant1.getId());
        assertThat(backupTenant1).isEqualTo(backupTenant2);

        backupTenant2 = getBackupTenantSample2();
        assertThat(backupTenant1).isNotEqualTo(backupTenant2);
    }

    @Test
    void settingTest() throws Exception {
        BackupTenant backupTenant = getBackupTenantRandomSampleGenerator();
        BackupTenantSetting backupTenantSettingBack = getBackupTenantSettingRandomSampleGenerator();

        backupTenant.setSetting(backupTenantSettingBack);
        assertThat(backupTenant.getSetting()).isEqualTo(backupTenantSettingBack);

        backupTenant.setting(null);
        assertThat(backupTenant.getSetting()).isNull();
    }

    @Test
    void backupFilesTest() throws Exception {
        BackupTenant backupTenant = getBackupTenantRandomSampleGenerator();
        BackupFile backupFileBack = getBackupFileRandomSampleGenerator();

        backupTenant.addBackupFiles(backupFileBack);
        assertThat(backupTenant.getBackupFiles()).containsOnly(backupFileBack);
        assertThat(backupFileBack.getTenant()).isEqualTo(backupTenant);

        backupTenant.removeBackupFiles(backupFileBack);
        assertThat(backupTenant.getBackupFiles()).doesNotContain(backupFileBack);
        assertThat(backupFileBack.getTenant()).isNull();

        backupTenant.backupFiles(new HashSet<>(Set.of(backupFileBack)));
        assertThat(backupTenant.getBackupFiles()).containsOnly(backupFileBack);
        assertThat(backupFileBack.getTenant()).isEqualTo(backupTenant);

        backupTenant.setBackupFiles(new HashSet<>());
        assertThat(backupTenant.getBackupFiles()).doesNotContain(backupFileBack);
        assertThat(backupFileBack.getTenant()).isNull();
    }
}
