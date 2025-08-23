package org.khasanof.backup.service;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.service.dto.BackupFileDTO;
import org.khasanof.core.service.base.IGeneralService;

/**
 * Service Interface for managing {@link org.khasanof.backup.domain.common.BackupFile}.
 */
public interface BackupFileService extends IGeneralService<BackupFile, BackupFileDTO> {
}
