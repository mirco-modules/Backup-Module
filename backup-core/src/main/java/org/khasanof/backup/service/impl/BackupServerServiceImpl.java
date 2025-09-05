package org.khasanof.backup.service.impl;

import org.khasanof.backup.domain.common.BackupServer;
import org.khasanof.backup.service.BackupServerService;
import org.khasanof.backup.service.dto.BackupServerDTO;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.khasanof.core.service.base.impl.GeneralValidateService;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.khasanof.core.service.validator.manager.IGeneralValidatorManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.impl
 * @since 9/5/25
 */
@Service
@Transactional
public class BackupServerServiceImpl extends GeneralValidateService<BackupServer, BackupServerDTO> implements BackupServerService {

    public BackupServerServiceImpl(IGeneralMapper<BackupServer, BackupServerDTO> generalMapper,
                                   IGeneralRepository<BackupServer> generalRepository,
                                   IGeneralValidatorManager generalValidatorManager
    ) {
        super(generalMapper, generalRepository, generalValidatorManager);
    }
}
