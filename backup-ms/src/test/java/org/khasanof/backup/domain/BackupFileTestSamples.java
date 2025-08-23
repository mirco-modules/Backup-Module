package org.khasanof.backup.domain;

import org.khasanof.backup.domain.common.BackupFile;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BackupFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BackupFile getBackupFileSample1() {
        return new BackupFile().id(1L).filePath("filePath1").fileSize(1L);
    }

    public static BackupFile getBackupFileSample2() {
        return new BackupFile().id(2L).filePath("filePath2").fileSize(2L);
    }

    public static BackupFile getBackupFileRandomSampleGenerator() {
        return new BackupFile()
            .id(longCount.incrementAndGet())
            .filePath(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet());
    }
}
