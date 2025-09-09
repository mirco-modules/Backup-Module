package org.khasanof.backup.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.service.criteria.BackupFileCriteria;
import org.khasanof.backup.service.dto.BackupFileDTO;
import org.khasanof.backup.service.feature.restore.RestoreBackupFileService;
import org.khasanof.core.service.base.IGeneralService;
import org.khasanof.core.service.query.base.IGeneralQueryService;
import org.khasanof.core.web.rest.base.GeneralQueryResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link org.khasanof.backup.domain.common.BackupFile}.
 */
@Slf4j
@RestController
@RequestMapping("/api/backup-files")
public class BackupFileResource extends GeneralQueryResource<BackupFile, BackupFileDTO, BackupFileCriteria> {

    private final RestoreBackupFileService restoreBackupFileService;

    public BackupFileResource(IGeneralService<BackupFile, BackupFileDTO> generalService,
                              IGeneralQueryService<BackupFile, BackupFileDTO, BackupFileCriteria> generalQueryService,
                              RestoreBackupFileService restoreBackupFileService
    ) {
        super(generalService, generalQueryService);
        this.restoreBackupFileService = restoreBackupFileService;
    }

    /**
     * This REST API used for restore backup files
     *
     * @param id back file id
     * @return
     */
    @GetMapping("/restore/{id}")
    public ResponseEntity<Void> restoreBackupFile(@PathVariable Long id) {
        log.debug("REST request to restore BackupFile : {}", id);
        restoreBackupFileService.restoreBackupFile(id);
        return ResponseEntity.ok()
                .build();
    }
}
