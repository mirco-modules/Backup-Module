package org.khasanof.backup.service.query;

import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.service.criteria.BackupTenantSettingCriteria;
import org.khasanof.backup.service.dto.BackupTenantSettingDTO;
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
 * Service for executing complex queries for {@link BackupTenantSetting} entities in the database.
 * The main input is a {@link BackupTenantSettingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BackupTenantSettingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BackupTenantSettingQueryService extends DynamicSpecificationQueryService<BackupTenantSetting, BackupTenantSettingDTO, BackupTenantSettingCriteria> {

    public BackupTenantSettingQueryService(IGeneralMapper<BackupTenantSetting, BackupTenantSettingDTO> generalMapper,
                                           IGeneralRepository<BackupTenantSetting> generalRepository,
                                           CriteriaFieldResolver criteriaFieldResolver,
                                           DynamicSpecificationBuilderManager dynamicSpecificationBuilderManager
    ) {
        super(generalMapper, generalRepository, criteriaFieldResolver, dynamicSpecificationBuilderManager);
    }
}
