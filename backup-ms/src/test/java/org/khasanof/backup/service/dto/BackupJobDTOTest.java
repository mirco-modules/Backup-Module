package org.khasanof.backup.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.khasanof.backup.web.rest.TestUtil;

class BackupJobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupJobDTO.class);
        BackupJobDTO backupJobDTO1 = new BackupJobDTO();
        backupJobDTO1.setId(1L);
        BackupJobDTO backupJobDTO2 = new BackupJobDTO();
        assertThat(backupJobDTO1).isNotEqualTo(backupJobDTO2);
        backupJobDTO2.setId(backupJobDTO1.getId());
        assertThat(backupJobDTO1).isEqualTo(backupJobDTO2);
        backupJobDTO2.setId(2L);
        assertThat(backupJobDTO1).isNotEqualTo(backupJobDTO2);
        backupJobDTO1.setId(null);
        assertThat(backupJobDTO1).isNotEqualTo(backupJobDTO2);
    }
}
