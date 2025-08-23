package org.khasanof.backup.service.impl;

import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.service.BackupTenantService;
import org.khasanof.backup.service.dto.BackupTenantDTO;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.khasanof.core.service.base.impl.GeneralValidateService;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.khasanof.core.service.validator.manager.IGeneralValidatorManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.khasanof.backup.domain.common.BackupTenant}.
 */
@Service
@Transactional
public class BackupTenantServiceImpl extends GeneralValidateService<BackupTenant, BackupTenantDTO> implements BackupTenantService {

    public BackupTenantServiceImpl(IGeneralMapper<BackupTenant, BackupTenantDTO> generalMapper,
                                   IGeneralRepository<BackupTenant> generalRepository,
                                   IGeneralValidatorManager generalValidatorManager
    ) {
        super(generalMapper, generalRepository, generalValidatorManager);
    }
}
