package org.khasanof.backup.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.khasanof.backup.web.rest.TestUtil;

class BackupTenantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupTenantDTO.class);
        BackupTenantDTO backupTenantDTO1 = new BackupTenantDTO();
        backupTenantDTO1.setId(1L);
        BackupTenantDTO backupTenantDTO2 = new BackupTenantDTO();
        assertThat(backupTenantDTO1).isNotEqualTo(backupTenantDTO2);
        backupTenantDTO2.setId(backupTenantDTO1.getId());
        assertThat(backupTenantDTO1).isEqualTo(backupTenantDTO2);
        backupTenantDTO2.setId(2L);
        assertThat(backupTenantDTO1).isNotEqualTo(backupTenantDTO2);
        backupTenantDTO1.setId(null);
        assertThat(backupTenantDTO1).isNotEqualTo(backupTenantDTO2);
    }
}
