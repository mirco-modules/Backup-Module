package org.khasanof.backup.web.rest;

import org.khasanof.backup.domain.common.BackupServer;
import org.khasanof.backup.service.criteria.BackupServerCriteria;
import org.khasanof.backup.service.dto.BackupServerDTO;
import org.khasanof.core.service.base.IGeneralService;
import org.khasanof.core.service.query.base.IGeneralQueryService;
import org.khasanof.core.web.rest.base.GeneralQueryResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link BackupServer}.
 */
@RestController
@RequestMapping("/api/backup-servers")
public class BackupServerResource extends GeneralQueryResource<BackupServer, BackupServerDTO, BackupServerCriteria> {

    public BackupServerResource(IGeneralService<BackupServer, BackupServerDTO> generalService, IGeneralQueryService<BackupServer, BackupServerDTO, BackupServerCriteria> generalQueryService) {
        super(generalService, generalQueryService);
    }
}
