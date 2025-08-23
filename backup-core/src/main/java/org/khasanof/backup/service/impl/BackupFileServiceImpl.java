package org.khasanof.backup.service.impl;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.service.BackupFileService;
import org.khasanof.backup.service.dto.BackupFileDTO;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.khasanof.core.service.base.impl.GeneralValidateService;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.khasanof.core.service.validator.manager.IGeneralValidatorManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.khasanof.backup.domain.common.BackupFile}.
 */
@Service
@Transactional
public class BackupFileServiceImpl extends GeneralValidateService<BackupFile, BackupFileDTO> implements BackupFileService {

    public BackupFileServiceImpl(IGeneralMapper<BackupFile, BackupFileDTO> generalMapper,
                                 IGeneralRepository<BackupFile> generalRepository,
                                 IGeneralValidatorManager generalValidatorManager
    ) {
        super(generalMapper, generalRepository, generalValidatorManager);
    }
}
