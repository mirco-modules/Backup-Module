package org.khasanof.backup.service.mapper;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.service.dto.BackupFileDTO;
import org.khasanof.backup.service.dto.BackupTenantDTO;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link BackupFile} and its DTO {@link BackupFileDTO}.
 */
@Mapper(componentModel = "spring")
public interface BackupFileMapper extends IGeneralMapper<BackupFile, BackupFileDTO> {

    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "backupTenantId")
    BackupFileDTO toDto(BackupFile s);

    @Named("backupTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BackupTenantDTO toDtoBackupTenantId(BackupTenant backupTenant);
}
