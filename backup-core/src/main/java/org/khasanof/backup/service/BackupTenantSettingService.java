package org.khasanof.backup.service;

import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.service.dto.BackupTenantSettingDTO;
import org.khasanof.core.service.base.IGeneralService;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.khasanof.backup.domain.common.BackupTenantSetting}.
 */
public interface BackupTenantSettingService extends IGeneralService<BackupTenantSetting, BackupTenantSettingDTO> {
}
