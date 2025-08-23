package org.khasanof.backup.config;

import liquibase.integration.spring.SpringLiquibase;
import org.khasanof.core.config.RootDataSourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.liquibase.SpringLiquibaseUtil;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
public class TestLiquibaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(TestLiquibaseConfiguration.class);

    private final Environment env;

    public TestLiquibaseConfiguration(Environment env) {
        this.env = env;
    }

    @Value("${application.liquibase.async-start:true}")
    private Boolean asyncStart;

    @Bean
    public SpringLiquibase liquibase(
        @Qualifier("taskExecutor") Executor executor,
        LiquibaseProperties liquibaseProperties,
        @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
        DataSource dataSource,
        DataSourceProperties dataSourceProperties
    ) {
        SpringLiquibase liquibase;
        if (Boolean.TRUE.equals(asyncStart)) {
            liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(
                this.env,
                executor,
                liquibaseDataSource.getIfAvailable(),
                liquibaseProperties,
                dataSource,
                dataSourceProperties
            );
        } else {
            liquibase = SpringLiquibaseUtil.createSpringLiquibase(
                liquibaseDataSource.getIfAvailable(),
                liquibaseProperties,
                dataSource,
                dataSourceProperties
            );
        }
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setLabelFilter(liquibaseProperties.getLabelFilter());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        if (env.matchesProfiles(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE)) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(liquibaseProperties.isEnabled());
            log.info("Configuring Test Liquibase For Datasource");
        }
        return liquibase;
    }
}
