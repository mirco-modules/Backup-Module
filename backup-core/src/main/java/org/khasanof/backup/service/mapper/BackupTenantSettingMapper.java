package org.khasanof.backup.service.mapper;

import org.khasanof.backup.domain.common.BackupTenantSetting;
import org.khasanof.backup.service.dto.BackupTenantSettingDTO;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link BackupTenantSetting} and its DTO {@link BackupTenantSettingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BackupTenantSettingMapper extends IGeneralMapper<BackupTenantSetting, BackupTenantSettingDTO> {}
