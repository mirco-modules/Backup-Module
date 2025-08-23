package org.khasanof.backup.config;

import org.khasanof.core.annotation.ScanPackages;
import org.springframework.stereotype.Component;

/**
 * @author Nurislom
 * @see org.khasanof.backup.config
 * @since 8/22/25
 */
@Component
@ScanPackages({"org.khasanof.core"})
public class ScanPackageConfiguration {
}
