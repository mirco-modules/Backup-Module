package org.khasanof.backup.service.feature.command.database.restore;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.core.domain.enumeration.DatabaseType;
import org.springframework.stereotype.Component;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.command.database.restore
 * @since 9/9/25
 */
@Component
public class GenerateRestoreSshCommandStrategyImpl implements GenerateRestoreSshCommandStrategy {

    /**
     *
     * @param backupFile
     * @param filepath
     * @return
     */
    @Override
    public String getPrefix(BackupFile backupFile, String filepath) {
        BackupTenant tenant = backupFile.getTenant();
        return "mysql -u " + tenant.getDbUsername() + " -p " + tenant.getDbPassword() + " " + tenant.getDbName() + " < " + filepath;
    }

    /**
     *
     * @return
     */
    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.MYSQL;
    }
}
