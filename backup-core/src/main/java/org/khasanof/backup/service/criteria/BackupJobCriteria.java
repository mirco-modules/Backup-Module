package org.khasanof.backup.service.criteria;

import org.khasanof.backup.domain.common.enumeration.BackupStatus;
import org.khasanof.core.annotation.query.JoinFilter;
import org.khasanof.core.service.criteria.base.ICriteria;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Criteria class for the {@link org.khasanof.backup.domain.common.BackupJob} entity. This class is used
 * in {@link org.khasanof.backup.web.rest.BackupJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /backup-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupJobCriteria implements Serializable, ICriteria {

    /**
     * Class for filtering BackupStatus
     */
    public static class BackupStatusFilter extends Filter<BackupStatus> {

        public BackupStatusFilter() {}

        public BackupStatusFilter(BackupStatusFilter filter) {
            super(filter);
        }

        @Override
        public BackupStatusFilter copy() {
            return new BackupStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter startedAt;

    private InstantFilter finishedAt;

    private BackupStatusFilter status;

    private StringFilter message;

    @JoinFilter(fieldName = "backupFile")
    private LongFilter backupFileId;

    private Boolean distinct;

    public BackupJobCriteria() {}

    public BackupJobCriteria(BackupJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.startedAt = other.optionalStartedAt().map(InstantFilter::copy).orElse(null);
        this.finishedAt = other.optionalFinishedAt().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(BackupStatusFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.backupFileId = other.optionalBackupFileId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BackupJobCriteria copy() {
        return new BackupJobCriteria(this);
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

    public InstantFilter getStartedAt() {
        return startedAt;
    }

    public Optional<InstantFilter> optionalStartedAt() {
        return Optional.ofNullable(startedAt);
    }

    public InstantFilter startedAt() {
        if (startedAt == null) {
            setStartedAt(new InstantFilter());
        }
        return startedAt;
    }

    public void setStartedAt(InstantFilter startedAt) {
        this.startedAt = startedAt;
    }

    public InstantFilter getFinishedAt() {
        return finishedAt;
    }

    public Optional<InstantFilter> optionalFinishedAt() {
        return Optional.ofNullable(finishedAt);
    }

    public InstantFilter finishedAt() {
        if (finishedAt == null) {
            setFinishedAt(new InstantFilter());
        }
        return finishedAt;
    }

    public void setFinishedAt(InstantFilter finishedAt) {
        this.finishedAt = finishedAt;
    }

    public BackupStatusFilter getStatus() {
        return status;
    }

    public Optional<BackupStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public BackupStatusFilter status() {
        if (status == null) {
            setStatus(new BackupStatusFilter());
        }
        return status;
    }

    public void setStatus(BackupStatusFilter status) {
        this.status = status;
    }

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public LongFilter getBackupFileId() {
        return backupFileId;
    }

    public Optional<LongFilter> optionalBackupFileId() {
        return Optional.ofNullable(backupFileId);
    }

    public LongFilter backupFileId() {
        if (backupFileId == null) {
            setBackupFileId(new LongFilter());
        }
        return backupFileId;
    }

    public void setBackupFileId(LongFilter backupFileId) {
        this.backupFileId = backupFileId;
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
        final BackupJobCriteria that = (BackupJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startedAt, that.startedAt) &&
            Objects.equals(finishedAt, that.finishedAt) &&
            Objects.equals(status, that.status) &&
            Objects.equals(message, that.message) &&
            Objects.equals(backupFileId, that.backupFileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startedAt, finishedAt, status, message, backupFileId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStartedAt().map(f -> "startedAt=" + f + ", ").orElse("") +
            optionalFinishedAt().map(f -> "finishedAt=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalBackupFileId().map(f -> "backupFileId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
