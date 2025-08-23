package org.khasanof.backup.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.khasanof.backup.web.rest.TestUtil;

class BackupTenantSettingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupTenantSettingDTO.class);
        BackupTenantSettingDTO backupTenantSettingDTO1 = new BackupTenantSettingDTO();
        backupTenantSettingDTO1.setId(1L);
        BackupTenantSettingDTO backupTenantSettingDTO2 = new BackupTenantSettingDTO();
        assertThat(backupTenantSettingDTO1).isNotEqualTo(backupTenantSettingDTO2);
        backupTenantSettingDTO2.setId(backupTenantSettingDTO1.getId());
        assertThat(backupTenantSettingDTO1).isEqualTo(backupTenantSettingDTO2);
        backupTenantSettingDTO2.setId(2L);
        assertThat(backupTenantSettingDTO1).isNotEqualTo(backupTenantSettingDTO2);
        backupTenantSettingDTO1.setId(null);
        assertThat(backupTenantSettingDTO1).isNotEqualTo(backupTenantSettingDTO2);
    }
}
