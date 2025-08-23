package org.khasanof.backup.domain;

import org.khasanof.backup.domain.common.BackupTenant;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BackupTenantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BackupTenant getBackupTenantSample1() {
        return new BackupTenant()
            .id(1L)
            .tenantKey("tenantKey1")
            .dbName("dbName1")
            .dbHost("dbHost1")
            .dbPort(1)
            .dbUsername("dbUsername1")
            .dbPassword("dbPassword1");
    }

    public static BackupTenant getBackupTenantSample2() {
        return new BackupTenant()
            .id(2L)
            .tenantKey("tenantKey2")
            .dbName("dbName2")
            .dbHost("dbHost2")
            .dbPort(2)
            .dbUsername("dbUsername2")
            .dbPassword("dbPassword2");
    }

    public static BackupTenant getBackupTenantRandomSampleGenerator() {
        return new BackupTenant()
            .id(longCount.incrementAndGet())
            .tenantKey(UUID.randomUUID().toString())
            .dbName(UUID.randomUUID().toString())
            .dbHost(UUID.randomUUID().toString())
            .dbPort(intCount.incrementAndGet())
            .dbUsername(UUID.randomUUID().toString())
            .dbPassword(UUID.randomUUID().toString());
    }
}
