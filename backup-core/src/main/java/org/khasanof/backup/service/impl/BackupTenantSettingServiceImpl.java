package org.khasanof.backup.service.impl;

import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.service.BackupTenantSettingService;
import org.khasanof.backup.service.dto.BackupTenantSettingDTO;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.khasanof.core.service.base.impl.GeneralValidateService;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.khasanof.core.service.validator.manager.IGeneralValidatorManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.khasanof.backup.domain.common.BackupTenantSetting}.
 */
@Service
@Transactional
public class BackupTenantSettingServiceImpl extends GeneralValidateService<BackupTenantSetting, BackupTenantSettingDTO> implements BackupTenantSettingService {

    public BackupTenantSettingServiceImpl(IGeneralMapper<BackupTenantSetting, BackupTenantSettingDTO> generalMapper,
                                          IGeneralRepository<BackupTenantSetting> generalRepository,
                                          IGeneralValidatorManager generalValidatorManager
    ) {
        super(generalMapper, generalRepository, generalValidatorManager);
    }
}
