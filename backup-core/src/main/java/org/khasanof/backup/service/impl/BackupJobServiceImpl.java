package org.khasanof.backup.service.impl;

import org.khasanof.backup.domain.common.BackupJob;
import org.khasanof.backup.service.BackupJobService;
import org.khasanof.backup.service.dto.BackupJobDTO;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.khasanof.core.service.base.impl.GeneralValidateService;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.khasanof.core.service.validator.manager.IGeneralValidatorManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.khasanof.backup.domain.common.BackupJob}.
 */
@Service
@Transactional
public class BackupJobServiceImpl extends GeneralValidateService<BackupJob, BackupJobDTO> implements BackupJobService {

    public BackupJobServiceImpl(IGeneralMapper<BackupJob, BackupJobDTO> generalMapper,
                                IGeneralRepository<BackupJob> generalRepository,
                                IGeneralValidatorManager generalValidatorManager
    ) {
        super(generalMapper, generalRepository, generalValidatorManager);
    }
}
