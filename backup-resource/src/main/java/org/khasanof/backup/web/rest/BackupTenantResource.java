package org.khasanof.backup.web.rest;

import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.service.criteria.BackupTenantCriteria;
import org.khasanof.backup.service.dto.BackupTenantDTO;
import org.khasanof.core.service.base.IGeneralService;
import org.khasanof.core.service.query.base.IGeneralQueryService;
import org.khasanof.core.web.rest.base.GeneralQueryResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link org.khasanof.backup.domain.common.BackupTenant}.
 */
@RestController
@RequestMapping("/api/backup-tenants")
public class BackupTenantResource extends GeneralQueryResource<BackupTenant, BackupTenantDTO, BackupTenantCriteria> {

    public BackupTenantResource(IGeneralService<BackupTenant, BackupTenantDTO> generalService,
                                IGeneralQueryService<BackupTenant, BackupTenantDTO, BackupTenantCriteria> generalQueryService
    ) {
        super(generalService, generalQueryService);
    }
}
