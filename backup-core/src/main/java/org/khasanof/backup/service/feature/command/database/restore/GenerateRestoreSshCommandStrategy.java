package org.khasanof.backup.service.feature.command.database.restore;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.core.domain.enumeration.DatabaseType;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.command.database.restore
 * @since 9/9/25
 */
public interface GenerateRestoreSshCommandStrategy {

    /**
     *
     * @param backupFile
     * @param filepath
     * @return
     */
    String getPrefix(BackupFile backupFile, String filepath);

    /**
     *
     * @return
     */
    DatabaseType getDatabaseType();
}
