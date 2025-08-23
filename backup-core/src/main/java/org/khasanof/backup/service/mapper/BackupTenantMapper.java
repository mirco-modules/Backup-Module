package org.khasanof.backup.service.mapper;

import org.khasanof.backup.domain.common.BackupTenant;
import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.service.dto.BackupTenantDTO;
import org.khasanof.backup.service.dto.BackupTenantSettingDTO;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link BackupTenant} and its DTO {@link BackupTenantDTO}.
 */
@Mapper(componentModel = "spring")
public interface BackupTenantMapper extends IGeneralMapper<BackupTenant, BackupTenantDTO> {

    @Mapping(target = "setting", source = "setting", qualifiedByName = "backupTenantSettingId")
    BackupTenantDTO toDto(BackupTenant s);

    @Named("backupTenantSettingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BackupTenantSettingDTO toDtoBackupTenantSettingId(BackupTenantSetting backupTenantSetting);
}
