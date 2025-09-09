package org.khasanof.backup.service.feature.command;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.domain.common.BackupTenant;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.command
 * @since 9/5/25
 */
public interface SshCommandFactory {

    /**
     *
     * @param tenant
     * @return
     */
    String createBackupCommand(BackupTenant tenant);

    /**
     *
     * @param backupFile
     * @return
     */
    String createRestoreCommand(BackupFile backupFile);
}
