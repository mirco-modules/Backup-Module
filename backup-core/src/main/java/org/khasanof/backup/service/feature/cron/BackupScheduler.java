package org.khasanof.backup.service.feature.cron;

import lombok.RequiredArgsConstructor;
import org.khasanof.backup.service.feature.backup.CronBackupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.cron
 * @since 9/5/25
 */
@Component
@RequiredArgsConstructor
public class BackupScheduler {

    private final CronBackupService cronBackupService;

    /**
     *
     */
    @Scheduled(cron = "0 0 * * * *")
    public void backupSchedule() {
        cronBackupService.backupForAllTenants();
    }
}
