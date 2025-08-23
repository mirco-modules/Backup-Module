package org.khasanof.backup.domain;

import org.khasanof.backup.domain.common.BackupTenantSetting;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BackupTenantSettingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BackupTenantSetting getBackupTenantSettingSample1() {
        return new BackupTenantSetting().id(1L).duration(1);
    }

    public static BackupTenantSetting getBackupTenantSettingSample2() {
        return new BackupTenantSetting().id(2L).duration(2);
    }

    public static BackupTenantSetting getBackupTenantSettingRandomSampleGenerator() {
        return new BackupTenantSetting().id(longCount.incrementAndGet()).duration(intCount.incrementAndGet());
    }
}
