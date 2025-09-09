package org.khasanof.backup.service.feature.command;

import lombok.RequiredArgsConstructor;
import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.service.feature.command.database.manager.GenerateSshCommandStrategyManager;
import org.khasanof.backup.service.feature.command.filepath.SshCommandFilepathService;
import org.springframework.stereotype.Service;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.command
 * @since 9/5/25
 */
@Service
@RequiredArgsConstructor
public class SshCommandFactoryImpl implements SshCommandFactory {

    private final SshCommandFilepathService sshCommandFilepathService;
    private final GenerateSshCommandStrategyManager generateSshCommandStrategyManager;

    /**
     *
     * @param tenant
     * @return
     */
    @Override
    public String createBackupCommand(BackupTenant tenant) {
        String filepath = sshCommandFilepathService.getFilepath(tenant);
        return generateSshCommandStrategyManager.getBackupCommandPrefix(tenant, filepath);
    }

    /**
     *
     * @param backupFile
     * @return
     */
    @Override
    public String createRestoreCommand(BackupFile backupFile) {
        return generateSshCommandStrategyManager.getRestoreCommandPrefix(backupFile, backupFile.getFilePath());
    }
}
