package org.khasanof.backup.service;

import org.khasanof.backup.domain.common.BackupJob;
import org.khasanof.backup.service.dto.BackupJobDTO;
import org.khasanof.core.service.base.IGeneralService;

/**
 * Service Interface for managing {@link org.khasanof.backup.domain.common.BackupJob}.
 */
public interface BackupJobService extends IGeneralService<BackupJob, BackupJobDTO> {
}
