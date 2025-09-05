package org.khasanof.backup.service.criteria;

import org.khasanof.backup.domain.common.enumeration.BackupServerStatus;
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
 * Criteria class for the {@link org.khasanof.backup.domain.common.BackupServer} entity. This class is used
 * in {@link org.khasanof.backup.web.rest.BackupServerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /backup-servers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupServerCriteria implements Serializable, ICriteria {

    /**
     * Class for filtering BackupServerStatus
     */
    public static class BackupServerStatusFilter extends Filter<BackupServerStatus> {

        public BackupServerStatusFilter() {}

        public BackupServerStatusFilter(BackupServerStatusFilter filter) {
            super(filter);
        }

        @Override
        public BackupServerStatusFilter copy() {
            return new BackupServerStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter port;

    private BackupServerStatusFilter status;

    private StringFilter host;

    private StringFilter username;

    private StringFilter password;

    private Boolean distinct;

    public BackupServerCriteria() {}

    public BackupServerCriteria(BackupServerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.port = other.optionalPort().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(BackupServerStatusFilter::copy).orElse(null);
        this.host = other.optionalHost().map(StringFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.password = other.optionalPassword().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BackupServerCriteria copy() {
        return new BackupServerCriteria(this);
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

    public IntegerFilter getPort() {
        return port;
    }

    public Optional<IntegerFilter> optionalPort() {
        return Optional.ofNullable(port);
    }

    public IntegerFilter port() {
        if (port == null) {
            setPort(new IntegerFilter());
        }
        return port;
    }

    public void setPort(IntegerFilter port) {
        this.port = port;
    }

    public BackupServerStatusFilter getStatus() {
        return status;
    }

    public Optional<BackupServerStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public BackupServerStatusFilter status() {
        if (status == null) {
            setStatus(new BackupServerStatusFilter());
        }
        return status;
    }

    public void setStatus(BackupServerStatusFilter status) {
        this.status = status;
    }

    public StringFilter getHost() {
        return host;
    }

    public Optional<StringFilter> optionalHost() {
        return Optional.ofNullable(host);
    }

    public StringFilter host() {
        if (host == null) {
            setHost(new StringFilter());
        }
        return host;
    }

    public void setHost(StringFilter host) {
        this.host = host;
    }

    public StringFilter getUsername() {
        return username;
    }

    public Optional<StringFilter> optionalUsername() {
        return Optional.ofNullable(username);
    }

    public StringFilter username() {
        if (username == null) {
            setUsername(new StringFilter());
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getPassword() {
        return password;
    }

    public Optional<StringFilter> optionalPassword() {
        return Optional.ofNullable(password);
    }

    public StringFilter password() {
        if (password == null) {
            setPassword(new StringFilter());
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
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
        final BackupServerCriteria that = (BackupServerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(port, that.port) &&
            Objects.equals(status, that.status) &&
            Objects.equals(host, that.host) &&
            Objects.equals(username, that.username) &&
            Objects.equals(password, that.password) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, port, status, host, username, password, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupServerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPort().map(f -> "port=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalHost().map(f -> "host=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalPassword().map(f -> "password=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
