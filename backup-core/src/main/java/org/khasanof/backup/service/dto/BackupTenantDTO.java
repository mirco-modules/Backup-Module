package org.khasanof.backup.service.dto;

import jakarta.validation.constraints.NotNull;
import org.khasanof.core.service.dto.base.IDto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.khasanof.backup.domain.common.BackupTenant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupTenantDTO implements IDto {

    private Long id;

    @NotNull
    private String tenantKey;

    @NotNull
    private String dbName;

    @NotNull
    private String dbHost;

    @NotNull
    private Integer dbPort;

    @NotNull
    private String dbUsername;

    @NotNull
    private String dbPassword;

    private BackupTenantSettingDTO setting;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantKey() {
        return tenantKey;
    }

    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public void setDbPort(Integer dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public BackupTenantSettingDTO getSetting() {
        return setting;
    }

    public void setSetting(BackupTenantSettingDTO setting) {
        this.setting = setting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupTenantDTO)) {
            return false;
        }

        BackupTenantDTO backupTenantDTO = (BackupTenantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, backupTenantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupTenantDTO{" +
            "id=" + getId() +
            ", tenantKey='" + getTenantKey() + "'" +
            ", dbName='" + getDbName() + "'" +
            ", dbHost='" + getDbHost() + "'" +
            ", dbPort=" + getDbPort() +
            ", dbUsername='" + getDbUsername() + "'" +
            ", dbPassword='" + getDbPassword() + "'" +
            ", setting=" + getSetting() +
            "}";
    }
}
