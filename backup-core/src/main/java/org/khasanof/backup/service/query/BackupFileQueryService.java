package org.khasanof.backup.service.query;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.service.criteria.BackupFileCriteria;
import org.khasanof.backup.service.dto.BackupFileDTO;
import org.khasanof.core.repository.base.IGeneralRepository;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.khasanof.core.service.query.specification.DynamicSpecificationQueryService;
import org.khasanof.core.service.query.specification.core.helper.CriteriaFieldResolver;
import org.khasanof.core.service.query.specification.core.manager.DynamicSpecificationBuilderManager;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link BackupFile} entities in the database.
 * The main input is a {@link BackupFileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BackupFileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BackupFileQueryService extends DynamicSpecificationQueryService<BackupFile, BackupFileDTO, BackupFileCriteria> {

    public BackupFileQueryService(IGeneralMapper<BackupFile, BackupFileDTO> generalMapper,
                                  IGeneralRepository<BackupFile> generalRepository,
                                  CriteriaFieldResolver criteriaFieldResolver,
                                  DynamicSpecificationBuilderManager dynamicSpecificationBuilderManager
    ) {
        super(generalMapper, generalRepository, criteriaFieldResolver, dynamicSpecificationBuilderManager);
    }
}
