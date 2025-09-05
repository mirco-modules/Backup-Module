package org.khasanof.backup.service.mapper;

import org.khasanof.backup.domain.common.BackupServer;
import org.khasanof.backup.service.dto.BackupServerDTO;
import org.khasanof.core.service.mapper.base.IGeneralMapper;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link org.khasanof.backup.domain.common.BackupServer} and its DTO {@link org.khasanof.backup.service.dto.BackupServerDTO}.
 *
 * @author Nurislom
 * @see org.khasanof.backup.service.mapper
 * @since 9/5/25
 */
@Mapper(componentModel = "spring")
public interface BackupServerMapper extends IGeneralMapper<BackupServer, BackupServerDTO> {
}
