package org.khasanof.backup.service.feature.backup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.backup.domain.common.BackupJob;
import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.domain.common.enumeration.BackupStatus;
import org.khasanof.backup.repository.common.BackupTenantRepository;
import org.khasanof.backup.service.feature.command.SshCommandFactory;
import org.khasanof.backup.service.feature.ssh.SshClientExecutorService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.backup
 * @since 9/5/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CronBackupServiceImpl implements CronBackupService {

    private final SshCommandFactory sshCommandFactory;
    private final BackupTenantRepository backupTenantRepository;
    private final SshClientExecutorService sshClientExecutorService;

    /**
     *
     */
    @Override
    public void backupForAllTenants() {
        // TODO create backup job
        // TODO create backup file
        List<BackupTenant> tenants = backupTenantRepository.findAll();
        tenants.forEach(this::backupForTenant);
    }

    /**
     *
     * @return
     */
    private BackupJob createBackupJob() {
        BackupJob backupJob = new BackupJob();
        backupJob.setStartedAt(Instant.now());
        backupJob.setMessage("Backup started");
        backupJob.setStatus(BackupStatus.RUNNING);
        return backupJob;
    }

    /**
     *
     * @param tenant
     */
    @Override
    public void backupForTenant(BackupTenant tenant) {
        BackupTenantSetting setting = tenant.getSetting();
        Duration duration = getDuration(setting);

        Instant lastModifiedDate = tenant.getLastModifiedDate();
        Instant now = Instant.now();

        Duration between = Duration.between(lastModifiedDate, now);
        if (between.compareTo(duration) > 0) {
            log.warn("The time for backup has not come yet : {}", tenant);
            return;
        }

        String command = sshCommandFactory.createBackupCommand(tenant);
        sshClientExecutorService.executeSshCommand(tenant, command);
    }

    /**
     *
     * @param tenantSetting
     * @return
     */
    private Duration getDuration(BackupTenantSetting tenantSetting) {
        return Duration.of(tenantSetting.getDuration(), tenantSetting.getTimeUnit().getChronoUnit());
    }
}
