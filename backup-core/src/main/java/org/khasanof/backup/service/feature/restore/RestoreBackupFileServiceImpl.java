package org.khasanof.backup.service.feature.restore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.repository.common.BackupFileRepository;
import org.khasanof.backup.service.feature.command.SshCommandFactory;
import org.khasanof.backup.service.feature.ssh.SshClientExecutorService;
import org.khasanof.core.errors.DefaultErrorKeys;
import org.khasanof.core.errors.exception.BadRequestAlertException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.restore
 * @since 9/9/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestoreBackupFileServiceImpl implements RestoreBackupFileService {

    private final SshCommandFactory sshCommandFactory;
    private final BackupFileRepository backupFileRepository;
    private final SshClientExecutorService sshClientExecutorService;

    /**
     *
     * @param backupFileId
     */
    @Override
    @Transactional
    public void restoreBackupFile(Long backupFileId) {
        BackupFile backupFile = backupFileRepository.findById(backupFileId)
                .orElseThrow(() -> new BadRequestAlertException(DefaultErrorKeys.NOT_FOUND, "BackupFile"));

        String restoreCommand = sshCommandFactory.createRestoreCommand(backupFile);
        sshClientExecutorService.executeSshCommand(backupFile.getTenant(), restoreCommand);
    }
}
