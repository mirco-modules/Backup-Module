package org.khasanof.backup.service.mapper;

import org.khasanof.backup.domain.common.BackupFile;
import org.khasanof.backup.domain.common.BackupJob;
import org.khasanof.backup.service.dto.BackupFileDTO;
import org.khasanof.backup.service.dto.BackupJobDTO;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link BackupJob} and its DTO {@link BackupJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface BackupJobMapper extends IGeneralMapper<BackupJob, BackupJobDTO> {

    @Mapping(target = "backupFile", source = "backupFile", qualifiedByName = "backupFileId")
    BackupJobDTO toDto(BackupJob s);

    @Named("backupFileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BackupFileDTO toDtoBackupFileId(BackupFile backupFile);
}
