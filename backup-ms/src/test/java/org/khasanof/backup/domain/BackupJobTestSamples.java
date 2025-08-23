package org.khasanof.backup.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BackupJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BackupJob getBackupJobSample1() {
        return new BackupJob().id(1L).message("message1");
    }

    public static BackupJob getBackupJobSample2() {
        return new BackupJob().id(2L).message("message2");
    }

    public static BackupJob getBackupJobRandomSampleGenerator() {
        return new BackupJob().id(longCount.incrementAndGet()).message(UUID.randomUUID().toString());
    }
}
