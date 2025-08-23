package org.khasanof.backup.service.query;

import org.khasanof.backup.domain.common.BackupJob;
import org.khasanof.backup.service.criteria.BackupJobCriteria;
import org.khasanof.backup.service.dto.BackupJobDTO;
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
 * Service for executing complex queries for {@link BackupJob} entities in the database.
 * The main input is a {@link BackupJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BackupJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BackupJobQueryService extends DynamicSpecificationQueryService<BackupJob, BackupJobDTO, BackupJobCriteria> {

    public BackupJobQueryService(IGeneralMapper<BackupJob, BackupJobDTO> generalMapper,
                                 IGeneralRepository<BackupJob> generalRepository,
                                 CriteriaFieldResolver criteriaFieldResolver,
                                 DynamicSpecificationBuilderManager dynamicSpecificationBuilderManager
    ) {
        super(generalMapper, generalRepository, criteriaFieldResolver, dynamicSpecificationBuilderManager);
    }
}
