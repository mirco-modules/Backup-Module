package org.khasanof.backup.service.feature.command.database;

import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.core.domain.enumeration.DatabaseType;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.command.database
 * @since 9/5/25
 */
public interface GenerateSshCommandStrategy {

    /**
     *
     * @param tenant
     * @param filepath
     * @return
     */
    String getPrefix(BackupTenant tenant, String filepath);

    /**
     *
     * @return
     */
    DatabaseType getDatabaseType();
}
