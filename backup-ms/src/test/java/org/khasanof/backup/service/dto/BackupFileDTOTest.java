package org.khasanof.backup.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.khasanof.backup.web.rest.TestUtil;

class BackupFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupFileDTO.class);
        BackupFileDTO backupFileDTO1 = new BackupFileDTO();
        backupFileDTO1.setId(1L);
        BackupFileDTO backupFileDTO2 = new BackupFileDTO();
        assertThat(backupFileDTO1).isNotEqualTo(backupFileDTO2);
        backupFileDTO2.setId(backupFileDTO1.getId());
        assertThat(backupFileDTO1).isEqualTo(backupFileDTO2);
        backupFileDTO2.setId(2L);
        assertThat(backupFileDTO1).isNotEqualTo(backupFileDTO2);
        backupFileDTO1.setId(null);
        assertThat(backupFileDTO1).isNotEqualTo(backupFileDTO2);
    }
}
