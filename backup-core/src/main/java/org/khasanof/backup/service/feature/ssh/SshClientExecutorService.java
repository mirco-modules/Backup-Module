package org.khasanof.backup.service.feature.ssh;

import org.khasanof.backup.domain.common.BackupTenant;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.ssh
 * @since 9/5/25
 */
public interface SshClientExecutorService {

    /**
     *
     * @param tenant
     * @param command
     */
    void executeSshCommand(BackupTenant tenant, String command);
}
