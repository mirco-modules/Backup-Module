package org.khasanof.backup.config;

import lombok.Data;
import org.khasanof.core.domain.enumeration.DatabaseType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Nurislom
 * @see org.khasanof.backup.config
 * @since 9/5/25
 */
@Data
@ConfigurationProperties(prefix = "backup")
public class BackupServiceProperties {

    private Long defaultTimeoutSeconds = 60L;
    private String backupPath = "backups/";
    private DatabaseType databaseType = DatabaseType.MYSQL;
}
