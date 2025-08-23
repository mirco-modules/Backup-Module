package org.khasanof.backup.web.rest;

import org.khasanof.backup.domain.common.BackupJob;
import org.khasanof.backup.service.criteria.BackupJobCriteria;
import org.khasanof.backup.service.dto.BackupJobDTO;
import org.khasanof.core.service.base.IGeneralService;
import org.khasanof.core.service.query.base.IGeneralQueryService;
import org.khasanof.core.web.rest.base.GeneralQueryResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link org.khasanof.backup.domain.common.BackupJob}.
 */
@RestController
@RequestMapping("/api/backup-jobs")
public class BackupJobResource extends GeneralQueryResource<BackupJob, BackupJobDTO, BackupJobCriteria> {

    public BackupJobResource(IGeneralService<BackupJob, BackupJobDTO> generalService,
                             IGeneralQueryService<BackupJob, BackupJobDTO, BackupJobCriteria> generalQueryService
    ) {
        super(generalService, generalQueryService);
    }
}
