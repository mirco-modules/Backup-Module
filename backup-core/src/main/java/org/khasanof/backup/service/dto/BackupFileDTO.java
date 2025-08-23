package org.khasanof.backup.service.dto;

import jakarta.validation.constraints.NotNull;
import org.khasanof.core.service.dto.base.IDto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link org.khasanof.backup.domain.common.BackupFile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupFileDTO implements IDto {

    private Long id;

    @NotNull
    private String filePath;

    @NotNull
    private Long fileSize;

    @NotNull
    private Instant createdAt;

    private BackupTenantDTO tenant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BackupTenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(BackupTenantDTO tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupFileDTO)) {
            return false;
        }

        BackupFileDTO backupFileDTO = (BackupFileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, backupFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupFileDTO{" +
            "id=" + getId() +
            ", filePath='" + getFilePath() + "'" +
            ", fileSize=" + getFileSize() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", tenant=" + getTenant() +
            "}";
    }
}
