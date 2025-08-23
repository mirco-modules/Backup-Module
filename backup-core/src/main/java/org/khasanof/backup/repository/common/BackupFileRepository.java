package org.khasanof.backup.repository.common;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BackupFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BackupFileRepository extends IGeneralRepository<BackupFile> {}
