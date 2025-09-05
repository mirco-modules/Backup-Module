package org.khasanof.backup.service.feature.command.database.manager;

import lombok.RequiredArgsConstructor;
import org.khasanof.backup.config.BackupServiceProperties;
import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.service.feature.command.database.GenerateSshCommandStrategy;
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

    private final Map<DatabaseType, GenerateSshCommandStrategy> strategies = new HashMap<>();

    /**
     *
     * @param tenant
     * @param filepath
     * @return
     */
    public String getPrefix(BackupTenant tenant, String filepath) {
        DatabaseType databaseType = backupServiceProperties.getDatabaseType();
        GenerateSshCommandStrategy generateSshCommandStrategy = strategies.get(databaseType);
        if (generateSshCommandStrategy == null) {
            return null;
        }
        return generateSshCommandStrategy.getPrefix(tenant, filepath);
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(GenerateSshCommandStrategy.class)
                .forEach((beanName, strategy) -> strategies.put(strategy.getDatabaseType(), strategy));
    }
}
