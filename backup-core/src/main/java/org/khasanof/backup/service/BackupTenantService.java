package org.khasanof.backup.service;

import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.service.dto.BackupTenantDTO;
import org.khasanof.core.service.base.IGeneralService;

/**
 * Service Interface for managing {@link org.khasanof.backup.domain.common.BackupTenant}.
 */
public interface BackupTenantService extends IGeneralService<BackupTenant, BackupTenantDTO> {
}
