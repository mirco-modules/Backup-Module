package org.khasanof.backup.service.feature.backup;

import org.khasanof.backup.domain.common.BackupTenant;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.backup
 * @since 9/5/25
 */
public interface CronBackupService {

    /**
     *
     */
    void backupForAllTenants();

    /**
     *
     * @param tenant
     */
    void backupForTenant(BackupTenant tenant);
}
