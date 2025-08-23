package org.khasanof.backup.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.khasanof.backup.domain.common.enumeration.BackupTimeUnit;
import org.khasanof.core.service.dto.base.IDto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.khasanof.backup.domain.common.BackupTenantSetting} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupTenantSettingDTO implements IDto {

    private Long id;

    @NotNull
    private BackupTimeUnit timeUnit;

    @NotNull
    @Min(value = 1)
    private Integer duration;

    @NotNull
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BackupTimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(BackupTimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupTenantSettingDTO)) {
            return false;
        }

        BackupTenantSettingDTO backupTenantSettingDTO = (BackupTenantSettingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, backupTenantSettingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupTenantSettingDTO{" +
            "id=" + getId() +
            ", timeUnit='" + getTimeUnit() + "'" +
            ", duration=" + getDuration() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
