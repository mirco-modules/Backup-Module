package org.khasanof.backup.repository.common;

import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BackupTenantSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BackupTenantSettingRepository extends IGeneralRepository<BackupTenantSetting> {}
