package org.khasanof.backup.web.rest;

import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.service.criteria.BackupTenantSettingCriteria;
import org.khasanof.backup.service.dto.BackupTenantSettingDTO;
import org.khasanof.core.service.base.IGeneralService;
import org.khasanof.core.service.query.base.IGeneralQueryService;
import org.khasanof.core.web.rest.base.GeneralQueryResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link org.khasanof.backup.domain.common.BackupTenantSetting}.
 */
@RestController
@RequestMapping("/api/backup-tenant-settings")
public class BackupTenantSettingResource extends GeneralQueryResource<BackupTenantSetting, BackupTenantSettingDTO, BackupTenantSettingCriteria> {

    public BackupTenantSettingResource(IGeneralService<BackupTenantSetting, BackupTenantSettingDTO> generalService,
                                       IGeneralQueryService<BackupTenantSetting, BackupTenantSettingDTO, BackupTenantSettingCriteria> generalQueryService
    ) {
        super(generalService, generalQueryService);
    }
}
