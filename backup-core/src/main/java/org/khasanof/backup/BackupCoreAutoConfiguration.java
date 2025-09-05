package org.khasanof.backup;

import org.khasanof.backup.config.BackupServiceProperties;
import org.khasanof.core.MsCoreAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author Nurislom
 * @see org.khasanof.backup
 * @since 8/22/25
 */
@ComponentScan(
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {TypeExcludeFilter.class}
        ), @ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {AutoConfigurationExcludeFilter.class}
        )}
)
@EnableConfigurationProperties(BackupServiceProperties.class)
@ImportAutoConfiguration({MsCoreAutoConfiguration.class})
public class BackupCoreAutoConfiguration {
}
