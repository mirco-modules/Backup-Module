package org.khasanof.backup.web.rest;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.service.criteria.BackupFileCriteria;
import org.khasanof.backup.service.dto.BackupFileDTO;
import org.khasanof.core.service.base.IGeneralService;
import org.khasanof.core.service.query.base.IGeneralQueryService;
import org.khasanof.core.web.rest.base.GeneralQueryResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link org.khasanof.backup.domain.common.BackupFile}.
 */
@RestController
@RequestMapping("/api/backup-files")
public class BackupFileResource extends GeneralQueryResource<BackupFile, BackupFileDTO, BackupFileCriteria> {

    public BackupFileResource(IGeneralService<BackupFile, BackupFileDTO> generalService,
                              IGeneralQueryService<BackupFile, BackupFileDTO, BackupFileCriteria> generalQueryService
    ) {
        super(generalService, generalQueryService);
    }
}
