package org.khasanof.backup.domain;

import org.junit.jupiter.api.Test;
import org.khasanof.backup.domain.common.BackupServer;
import org.khasanof.backup.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.backup.domain.BackupServerTestSamples.getBackupServerSample1;
import static org.khasanof.backup.domain.BackupServerTestSamples.getBackupServerSample2;

class BackupServerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BackupServer.class);
        BackupServer backupServer1 = getBackupServerSample1();
        BackupServer backupServer2 = new BackupServer();
        assertThat(backupServer1).isNotEqualTo(backupServer2);

        backupServer2.setId(backupServer1.getId());
        assertThat(backupServer1).isEqualTo(backupServer2);

        backupServer2 = getBackupServerSample2();
        assertThat(backupServer1).isNotEqualTo(backupServer2);
    }
}
