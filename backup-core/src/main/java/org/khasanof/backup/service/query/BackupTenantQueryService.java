package org.khasanof.backup.service.query;

import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.service.criteria.BackupTenantCriteria;
import org.khasanof.backup.service.dto.BackupTenantDTO;
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
 * Service for executing complex queries for {@link BackupTenant} entities in the database.
 * The main input is a {@link BackupTenantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BackupTenantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BackupTenantQueryService extends DynamicSpecificationQueryService<BackupTenant, BackupTenantDTO, BackupTenantCriteria> {

    public BackupTenantQueryService(IGeneralMapper<BackupTenant, BackupTenantDTO> generalMapper,
                                    IGeneralRepository<BackupTenant> generalRepository,
                                    CriteriaFieldResolver criteriaFieldResolver,
                                    DynamicSpecificationBuilderManager dynamicSpecificationBuilderManager
    ) {
        super(generalMapper, generalRepository, criteriaFieldResolver, dynamicSpecificationBuilderManager);
    }
}
