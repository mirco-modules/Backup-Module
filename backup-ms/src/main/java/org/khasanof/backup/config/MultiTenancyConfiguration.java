package org.khasanof.backup.config;

import org.khasanof.core.domain.enumeration.TenancyResolverType;
import org.khasanof.core.domain.enumeration.TenancyType;
import org.khasanof.core.tenancy.TenancyConfigurer;
import org.khasanof.core.tenancy.core.constants.TenancyConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Nurislom
 * @see org.khasanof.backup.config
 * @since 8/22/25
 */
@Profile({"dev", "prod"})
@Configuration(TenancyConstants.TENANCY_CONFIGURER)
public class MultiTenancyConfiguration implements TenancyConfigurer {

    /**
     *
     * @return
     */
    @Override
    public TenancyType getType() {
        return TenancyType.MULTI;
    }

    /**
     *
     * @return
     */
    @Override
    public TenancyResolverType getResolverType() {
        return TenancyResolverType.SECURITY;
    }
}
