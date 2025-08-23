package org.khasanof.backup.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.khasanof.backup.domain.common.enumeration.BackupTimeUnit;
import org.khasanof.core.annotation.Common;
import org.khasanof.core.domain.AbstractAuditingEntity;

/**
 * A BackupTenantSetting.
 */
@Entity
@Common
@Table(name = "backup_tenant_setting")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupTenantSetting extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "time_unit", nullable = false)
    private BackupTimeUnit timeUnit;

    @NotNull
    @Min(value = 1)
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @JsonIgnoreProperties(value = { "setting", "backupFiles" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "setting")
    private BackupTenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BackupTenantSetting id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BackupTimeUnit getTimeUnit() {
        return this.timeUnit;
    }

    public BackupTenantSetting timeUnit(BackupTimeUnit timeUnit) {
        this.setTimeUnit(timeUnit);
        return this;
    }

    public void setTimeUnit(BackupTimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public BackupTenantSetting duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public BackupTenantSetting isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public BackupTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(BackupTenant backupTenant) {
        if (this.tenant != null) {
            this.tenant.setSetting(null);
        }
        if (backupTenant != null) {
            backupTenant.setSetting(this);
        }
        this.tenant = backupTenant;
    }

    public BackupTenantSetting tenant(BackupTenant backupTenant) {
        this.setTenant(backupTenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupTenantSetting)) {
            return false;
        }
        return getId() != null && getId().equals(((BackupTenantSetting) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupTenantSetting{" +
            "id=" + getId() +
            ", timeUnit='" + getTimeUnit() + "'" +
            ", duration=" + getDuration() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
