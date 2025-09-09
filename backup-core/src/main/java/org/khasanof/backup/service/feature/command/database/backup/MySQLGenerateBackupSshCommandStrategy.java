package org.khasanof.backup.service.feature.command.database.backup;

import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.core.domain.enumeration.DatabaseType;
import org.springframework.stereotype.Component;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.command.database
 * @since 9/5/25
 */
@Component
public class MySQLGenerateBackupSshCommandStrategy implements GenerateBackupSshCommandStrategy {

    /**
     *
     * @param tenant
     * @param filepath
     * @return
     */
    @Override
    public String getPrefix(BackupTenant tenant, String filepath) {
        return "mysqldump -u " + tenant.getDbUsername() + " -p " + tenant.getDbPassword() + " " + tenant.getDbName() + " > " + filepath;
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
