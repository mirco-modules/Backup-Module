package org.khasanof.backup.repository.common;

import org.khasanof.backup.domain.common.BackupServer;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BackupServer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BackupServerRepository extends IGeneralRepository<BackupServer> {}
