package org.khasanof.backup.service.criteria;

import org.khasanof.core.annotation.query.JoinFilter;
import org.khasanof.core.service.criteria.base.ICriteria;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Criteria class for the {@link org.khasanof.backup.domain.common.BackupTenant} entity. This class is used
 * in {@link org.khasanof.backup.web.rest.BackupTenantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /backup-tenants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupTenantCriteria implements Serializable, ICriteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantKey;

    private StringFilter dbName;

    private StringFilter dbHost;

    private IntegerFilter dbPort;

    private StringFilter dbUsername;

    private StringFilter dbPassword;

    @JoinFilter(fieldName = "setting")
    private LongFilter settingId;

    @JoinFilter(fieldName = "backupFiles")
    private LongFilter backupFilesId;

    private Boolean distinct;

    public BackupTenantCriteria() {}

    public BackupTenantCriteria(BackupTenantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantKey = other.optionalTenantKey().map(StringFilter::copy).orElse(null);
        this.dbName = other.optionalDbName().map(StringFilter::copy).orElse(null);
        this.dbHost = other.optionalDbHost().map(StringFilter::copy).orElse(null);
        this.dbPort = other.optionalDbPort().map(IntegerFilter::copy).orElse(null);
        this.dbUsername = other.optionalDbUsername().map(StringFilter::copy).orElse(null);
        this.dbPassword = other.optionalDbPassword().map(StringFilter::copy).orElse(null);
        this.settingId = other.optionalSettingId().map(LongFilter::copy).orElse(null);
        this.backupFilesId = other.optionalBackupFilesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BackupTenantCriteria copy() {
        return new BackupTenantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantKey() {
        return tenantKey;
    }

    public Optional<StringFilter> optionalTenantKey() {
        return Optional.ofNullable(tenantKey);
    }

    public StringFilter tenantKey() {
        if (tenantKey == null) {
            setTenantKey(new StringFilter());
        }
        return tenantKey;
    }

    public void setTenantKey(StringFilter tenantKey) {
        this.tenantKey = tenantKey;
    }

    public StringFilter getDbName() {
        return dbName;
    }

    public Optional<StringFilter> optionalDbName() {
        return Optional.ofNullable(dbName);
    }

    public StringFilter dbName() {
        if (dbName == null) {
            setDbName(new StringFilter());
        }
        return dbName;
    }

    public void setDbName(StringFilter dbName) {
        this.dbName = dbName;
    }

    public StringFilter getDbHost() {
        return dbHost;
    }

    public Optional<StringFilter> optionalDbHost() {
        return Optional.ofNullable(dbHost);
    }

    public StringFilter dbHost() {
        if (dbHost == null) {
            setDbHost(new StringFilter());
        }
        return dbHost;
    }

    public void setDbHost(StringFilter dbHost) {
        this.dbHost = dbHost;
    }

    public IntegerFilter getDbPort() {
        return dbPort;
    }

    public Optional<IntegerFilter> optionalDbPort() {
        return Optional.ofNullable(dbPort);
    }

    public IntegerFilter dbPort() {
        if (dbPort == null) {
            setDbPort(new IntegerFilter());
        }
        return dbPort;
    }

    public void setDbPort(IntegerFilter dbPort) {
        this.dbPort = dbPort;
    }

    public StringFilter getDbUsername() {
        return dbUsername;
    }

    public Optional<StringFilter> optionalDbUsername() {
        return Optional.ofNullable(dbUsername);
    }

    public StringFilter dbUsername() {
        if (dbUsername == null) {
            setDbUsername(new StringFilter());
        }
        return dbUsername;
    }

    public void setDbUsername(StringFilter dbUsername) {
        this.dbUsername = dbUsername;
    }

    public StringFilter getDbPassword() {
        return dbPassword;
    }

    public Optional<StringFilter> optionalDbPassword() {
        return Optional.ofNullable(dbPassword);
    }

    public StringFilter dbPassword() {
        if (dbPassword == null) {
            setDbPassword(new StringFilter());
        }
        return dbPassword;
    }

    public void setDbPassword(StringFilter dbPassword) {
        this.dbPassword = dbPassword;
    }

    public LongFilter getSettingId() {
        return settingId;
    }

    public Optional<LongFilter> optionalSettingId() {
        return Optional.ofNullable(settingId);
    }

    public LongFilter settingId() {
        if (settingId == null) {
            setSettingId(new LongFilter());
        }
        return settingId;
    }

    public void setSettingId(LongFilter settingId) {
        this.settingId = settingId;
    }

    public LongFilter getBackupFilesId() {
        return backupFilesId;
    }

    public Optional<LongFilter> optionalBackupFilesId() {
        return Optional.ofNullable(backupFilesId);
    }

    public LongFilter backupFilesId() {
        if (backupFilesId == null) {
            setBackupFilesId(new LongFilter());
        }
        return backupFilesId;
    }

    public void setBackupFilesId(LongFilter backupFilesId) {
        this.backupFilesId = backupFilesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BackupTenantCriteria that = (BackupTenantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantKey, that.tenantKey) &&
            Objects.equals(dbName, that.dbName) &&
            Objects.equals(dbHost, that.dbHost) &&
            Objects.equals(dbPort, that.dbPort) &&
            Objects.equals(dbUsername, that.dbUsername) &&
            Objects.equals(dbPassword, that.dbPassword) &&
            Objects.equals(settingId, that.settingId) &&
            Objects.equals(backupFilesId, that.backupFilesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantKey, dbName, dbHost, dbPort, dbUsername, dbPassword, settingId, backupFilesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupTenantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTenantKey().map(f -> "tenantKey=" + f + ", ").orElse("") +
            optionalDbName().map(f -> "dbName=" + f + ", ").orElse("") +
            optionalDbHost().map(f -> "dbHost=" + f + ", ").orElse("") +
            optionalDbPort().map(f -> "dbPort=" + f + ", ").orElse("") +
            optionalDbUsername().map(f -> "dbUsername=" + f + ", ").orElse("") +
            optionalDbPassword().map(f -> "dbPassword=" + f + ", ").orElse("") +
            optionalSettingId().map(f -> "settingId=" + f + ", ").orElse("") +
            optionalBackupFilesId().map(f -> "backupFilesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
