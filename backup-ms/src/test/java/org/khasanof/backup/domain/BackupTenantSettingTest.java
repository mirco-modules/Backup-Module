package org.khasanof.backup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.backup.domain.BackupTenantSettingTestSamples.*;
import static org.khasanof.backup.domain.BackupTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.web.rest.TestUtil;

class BackupTenantSettingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupTenantSetting.class);
        BackupTenantSetting backupTenantSetting1 = getBackupTenantSettingSample1();
        BackupTenantSetting backupTenantSetting2 = new BackupTenantSetting();
        assertThat(backupTenantSetting1).isNotEqualTo(backupTenantSetting2);

        backupTenantSetting2.setId(backupTenantSetting1.getId());
        assertThat(backupTenantSetting1).isEqualTo(backupTenantSetting2);

        backupTenantSetting2 = getBackupTenantSettingSample2();
        assertThat(backupTenantSetting1).isNotEqualTo(backupTenantSetting2);
    }

    @Test
    void tenantTest() throws Exception {
        BackupTenantSetting backupTenantSetting = getBackupTenantSettingRandomSampleGenerator();
        BackupTenant backupTenantBack = getBackupTenantRandomSampleGenerator();

        backupTenantSetting.setTenant(backupTenantBack);
        assertThat(backupTenantSetting.getTenant()).isEqualTo(backupTenantBack);
        assertThat(backupTenantBack.getSetting()).isEqualTo(backupTenantSetting);

        backupTenantSetting.tenant(null);
        assertThat(backupTenantSetting.getTenant()).isNull();
        assertThat(backupTenantBack.getSetting()).isNull();
    }
}
