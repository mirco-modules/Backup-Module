package org.khasanof.backup.service.dto;

import org.junit.jupiter.api.Test;
import org.khasanof.backup.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class BackupServerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupServerDTO.class);
        BackupServerDTO backupServerDTO1 = new BackupServerDTO();
        backupServerDTO1.setId(1L);
        BackupServerDTO backupServerDTO2 = new BackupServerDTO();
        assertThat(backupServerDTO1).isNotEqualTo(backupServerDTO2);
        backupServerDTO2.setId(backupServerDTO1.getId());
        assertThat(backupServerDTO1).isEqualTo(backupServerDTO2);
        backupServerDTO2.setId(2L);
        assertThat(backupServerDTO1).isNotEqualTo(backupServerDTO2);
        backupServerDTO1.setId(null);
        assertThat(backupServerDTO1).isNotEqualTo(backupServerDTO2);
    }
}
