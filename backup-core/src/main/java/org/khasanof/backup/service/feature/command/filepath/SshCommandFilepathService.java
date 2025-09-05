package org.khasanof.backup.service.feature.command.filepath;

import org.khasanof.backup.domain.common.BackupTenant;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.command.filepath
 * @since 9/5/25
 */
public interface SshCommandFilepathService {

    /**
     *
     * @param tenant
     * @return
     */
    String getFilepath(BackupTenant tenant);
}
