package org.khasanof.backup.service.criteria;

import org.khasanof.backup.domain.common.enumeration.BackupTimeUnit;
import org.khasanof.core.annotation.query.JoinFilter;
import org.khasanof.core.service.criteria.base.ICriteria;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Criteria class for the {@link org.khasanof.backup.domain.common.BackupTenantSetting} entity. This class is used
 * in {@link org.khasanof.backup.web.rest.BackupTenantSettingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /backup-tenant-settings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupTenantSettingCriteria implements Serializable, ICriteria {

    /**
     * Class for filtering BackupTimeUnit
     */
    public static class BackupTimeUnitFilter extends Filter<BackupTimeUnit> {

        public BackupTimeUnitFilter() {}

        public BackupTimeUnitFilter(BackupTimeUnitFilter filter) {
            super(filter);
        }

        @Override
        public BackupTimeUnitFilter copy() {
            return new BackupTimeUnitFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BackupTimeUnitFilter timeUnit;

    private IntegerFilter duration;

    private BooleanFilter isActive;

    @JoinFilter(fieldName = "tenant")
    private LongFilter tenantId;

    private Boolean distinct;

    public BackupTenantSettingCriteria() {}

    public BackupTenantSettingCriteria(BackupTenantSettingCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.timeUnit = other.optionalTimeUnit().map(BackupTimeUnitFilter::copy).orElse(null);
        this.duration = other.optionalDuration().map(IntegerFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BackupTenantSettingCriteria copy() {
        return new BackupTenantSettingCriteria(this);
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

    public BackupTimeUnitFilter getTimeUnit() {
        return timeUnit;
    }

    public Optional<BackupTimeUnitFilter> optionalTimeUnit() {
        return Optional.ofNullable(timeUnit);
    }

    public BackupTimeUnitFilter timeUnit() {
        if (timeUnit == null) {
            setTimeUnit(new BackupTimeUnitFilter());
        }
        return timeUnit;
    }

    public void setTimeUnit(BackupTimeUnitFilter timeUnit) {
        this.timeUnit = timeUnit;
    }

    public IntegerFilter getDuration() {
        return duration;
    }

    public Optional<IntegerFilter> optionalDuration() {
        return Optional.ofNullable(duration);
    }

    public IntegerFilter duration() {
        if (duration == null) {
            setDuration(new IntegerFilter());
        }
        return duration;
    }

    public void setDuration(IntegerFilter duration) {
        this.duration = duration;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public Optional<LongFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public LongFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new LongFilter());
        }
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
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
        final BackupTenantSettingCriteria that = (BackupTenantSettingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(timeUnit, that.timeUnit) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeUnit, duration, isActive, tenantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupTenantSettingCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTimeUnit().map(f -> "timeUnit=" + f + ", ").orElse("") +
            optionalDuration().map(f -> "duration=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
