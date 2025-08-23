package org.khasanof.backup.repository.common;

import org.khasanof.backup.domain.common.BackupJob;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BackupJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BackupJobRepository extends IGeneralRepository<BackupJob> {}
