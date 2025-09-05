package org.khasanof.backup.service.feature.command;

import lombok.RequiredArgsConstructor;
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
    public String create(BackupTenant tenant) {
        String filepath = sshCommandFilepathService.getFilepath(tenant);
        return generateSshCommandStrategyManager.getPrefix(tenant, filepath);
    }
}
