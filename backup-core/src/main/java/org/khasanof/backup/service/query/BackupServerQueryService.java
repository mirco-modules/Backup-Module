package org.khasanof.backup.service.query;

import org.khasanof.backup.domain.common.BackupServer;
import org.khasanof.backup.service.criteria.BackupServerCriteria;
import org.khasanof.backup.service.dto.BackupServerDTO;
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
 * Service for executing complex queries for {@link BackupServer} entities in the database.
 * The main input is a {@link BackupServerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BackupServerDTO} which fulfills the criteria.
 *
 * @author Nurislom
 * @see org.khasanof.backup.service.query
 * @since 9/5/25
 */
@Service
@Transactional(readOnly = true)
public class BackupServerQueryService extends DynamicSpecificationQueryService<BackupServer, BackupServerDTO, BackupServerCriteria> {

    public BackupServerQueryService(IGeneralMapper<BackupServer, BackupServerDTO> generalMapper,
                                    IGeneralRepository<BackupServer> generalRepository,
                                    CriteriaFieldResolver criteriaFieldResolver,
                                    DynamicSpecificationBuilderManager dynamicSpecificationBuilderManager
    ) {
        super(generalMapper, generalRepository, criteriaFieldResolver, dynamicSpecificationBuilderManager);
    }
}
