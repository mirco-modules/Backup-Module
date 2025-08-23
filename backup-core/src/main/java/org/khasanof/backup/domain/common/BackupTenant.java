package org.khasanof.backup.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.khasanof.core.annotation.Common;
import org.khasanof.core.domain.AbstractAuditingEntity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A BackupTenant.
 */
@Entity
@Common
@Table(name = "backup_tenant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupTenant extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tenant_key", nullable = false, unique = true)
    private String tenantKey;

    @NotNull
    @Column(name = "db_name", nullable = false)
    private String dbName;

    @NotNull
    @Column(name = "db_host", nullable = false)
    private String dbHost;

    @NotNull
    @Column(name = "db_port", nullable = false)
    private Integer dbPort;

    @NotNull
    @Column(name = "db_username", nullable = false)
    private String dbUsername;

    @NotNull
    @Column(name = "db_password", nullable = false)
    private String dbPassword;

    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private BackupTenantSetting setting;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenant")
    @JsonIgnoreProperties(value = { "backupJob", "tenant" }, allowSetters = true)
    private Set<BackupFile> backupFiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BackupTenant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantKey() {
        return this.tenantKey;
    }

    public BackupTenant tenantKey(String tenantKey) {
        this.setTenantKey(tenantKey);
        return this;
    }

    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    public String getDbName() {
        return this.dbName;
    }

    public BackupTenant dbName(String dbName) {
        this.setDbName(dbName);
        return this;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbHost() {
        return this.dbHost;
    }

    public BackupTenant dbHost(String dbHost) {
        this.setDbHost(dbHost);
        return this;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public Integer getDbPort() {
        return this.dbPort;
    }

    public BackupTenant dbPort(Integer dbPort) {
        this.setDbPort(dbPort);
        return this;
    }

    public void setDbPort(Integer dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbUsername() {
        return this.dbUsername;
    }

    public BackupTenant dbUsername(String dbUsername) {
        this.setDbUsername(dbUsername);
        return this;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return this.dbPassword;
    }

    public BackupTenant dbPassword(String dbPassword) {
        this.setDbPassword(dbPassword);
        return this;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public BackupTenantSetting getSetting() {
        return this.setting;
    }

    public void setSetting(BackupTenantSetting backupTenantSetting) {
        this.setting = backupTenantSetting;
    }

    public BackupTenant setting(BackupTenantSetting backupTenantSetting) {
        this.setSetting(backupTenantSetting);
        return this;
    }

    public Set<BackupFile> getBackupFiles() {
        return this.backupFiles;
    }

    public void setBackupFiles(Set<BackupFile> backupFiles) {
        if (this.backupFiles != null) {
            this.backupFiles.forEach(i -> i.setTenant(null));
        }
        if (backupFiles != null) {
            backupFiles.forEach(i -> i.setTenant(this));
        }
        this.backupFiles = backupFiles;
    }

    public BackupTenant backupFiles(Set<BackupFile> backupFiles) {
        this.setBackupFiles(backupFiles);
        return this;
    }

    public BackupTenant addBackupFiles(BackupFile backupFile) {
        this.backupFiles.add(backupFile);
        backupFile.setTenant(this);
        return this;
    }

    public BackupTenant removeBackupFiles(BackupFile backupFile) {
        this.backupFiles.remove(backupFile);
        backupFile.setTenant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupTenant)) {
            return false;
        }
        return getId() != null && getId().equals(((BackupTenant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupTenant{" +
            "id=" + getId() +
            ", tenantKey='" + getTenantKey() + "'" +
            ", dbName='" + getDbName() + "'" +
            ", dbHost='" + getDbHost() + "'" +
            ", dbPort=" + getDbPort() +
            ", dbUsername='" + getDbUsername() + "'" +
            ", dbPassword='" + getDbPassword() + "'" +
            "}";
    }
}
