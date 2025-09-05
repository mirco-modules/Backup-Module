package org.khasanof.backup.domain;

import org.khasanof.backup.domain.common.BackupServer;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BackupServerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BackupServer getBackupServerSample1() {
        return new BackupServer().id(1L).port(1).host("host1").username("username1").password("password1");
    }

    public static BackupServer getBackupServerSample2() {
        return new BackupServer().id(2L).port(2).host("host2").username("username2").password("password2");
    }

    public static BackupServer getBackupServerRandomSampleGenerator() {
        return new BackupServer()
            .id(longCount.incrementAndGet())
            .port(intCount.incrementAndGet())
            .host(UUID.randomUUID().toString())
            .username(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString());
    }
}
