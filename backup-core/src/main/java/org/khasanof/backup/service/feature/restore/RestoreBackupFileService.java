package org.khasanof.backup.service.feature.restore;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.restore
 * @since 9/9/25
 */
public interface RestoreBackupFileService {

    /**
     *
     * @param backupFileId
     */
    void restoreBackupFile(Long backupFileId);
}
