package org.khasanof.backup.service.feature.command.database.manager;

import lombok.RequiredArgsConstructor;
import org.khasanof.backup.config.BackupServiceProperties;
import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.service.feature.command.database.backup.GenerateBackupSshCommandStrategy;
import org.khasanof.backup.service.feature.command.database.restore.GenerateRestoreSshCommandStrategy;
import org.khasanof.core.domain.enumeration.DatabaseType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.command.database.manager
 * @since 9/5/25
 */
@Service
@RequiredArgsConstructor
public class GenerateSshCommandStrategyManager implements InitializingBean {

    private final ApplicationContext applicationContext;
    private final BackupServiceProperties backupServiceProperties;

    private final Map<DatabaseType, GenerateBackupSshCommandStrategy> backupStrategies = new HashMap<>();
    private final Map<DatabaseType, GenerateRestoreSshCommandStrategy> restoreStrategies = new HashMap<>();

    /**
     *
     * @param tenant
     * @param filepath
     * @return
     */
    public String getBackupCommandPrefix(BackupTenant tenant, String filepath) {
        DatabaseType databaseType = backupServiceProperties.getDatabaseType();
        GenerateBackupSshCommandStrategy generateBackupSshCommandStrategy = backupStrategies.get(databaseType);
        if (generateBackupSshCommandStrategy == null) {
            return null;
        }
        return generateBackupSshCommandStrategy.getPrefix(tenant, filepath);
    }

    /**
     *
     * @param backupFile
     * @param filepath
     * @return
     */
    public String getRestoreCommandPrefix(BackupFile backupFile, String filepath) {
        DatabaseType databaseType = backupServiceProperties.getDatabaseType();
        GenerateRestoreSshCommandStrategy generateRestoreSshCommandStrategy = restoreStrategies.get(databaseType);
        if (generateRestoreSshCommandStrategy == null) {
            return null;
        }
        return generateRestoreSshCommandStrategy.getPrefix(backupFile, filepath);
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(GenerateBackupSshCommandStrategy.class)
                .forEach((beanName, strategy) -> backupStrategies.put(strategy.getDatabaseType(), strategy));

        applicationContext.getBeansOfType(GenerateRestoreSshCommandStrategy.class)
                .forEach((beanName, strategy) -> restoreStrategies.put(strategy.getDatabaseType(), strategy));
    }
}
