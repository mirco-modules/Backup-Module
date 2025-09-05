package org.khasanof.backup.service.feature.command.filepath;

import lombok.RequiredArgsConstructor;
import org.khasanof.backup.config.BackupServiceProperties;
import org.khasanof.backup.domain.common.BackupTenant;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.command.filepath
 * @since 9/5/25
 */
@Service
@RequiredArgsConstructor
public class SshCommandFilepathServiceImpl implements SshCommandFilepathService {

    private final BackupServiceProperties backupServiceProperties;

    /**
     *
     * @param tenant
     * @return
     */
    @Override
    public String getFilepath(BackupTenant tenant) {
        String backupPath = backupServiceProperties.getBackupPath();
        String dbName = tenant.getDbName();
        LocalDate localDate = LocalDate.now().minusDays(1);
        return backupPath + dbName + localDate + ".sql";
    }
}
