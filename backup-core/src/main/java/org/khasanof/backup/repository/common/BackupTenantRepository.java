package org.khasanof.backup.repository.common;

import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tenant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BackupTenantRepository extends IGeneralRepository<BackupTenant> {}
