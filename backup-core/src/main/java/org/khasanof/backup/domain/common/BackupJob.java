package org.khasanof.backup.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.khasanof.backup.domain.common.enumeration.BackupStatus;
import org.khasanof.core.annotation.Common;
import org.khasanof.core.domain.AbstractAuditingEntity;

import java.time.Instant;

/**
 * A BackupJob.
 */
@Entity
@Common
@Table(name = "backup_job")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupJob extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BackupStatus status;

    @Size(max = 1000)
    @Column(name = "message", length = 1000)
    private String message;

    @JsonIgnoreProperties(value = { "backupJob", "tenant" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private BackupFile backupFile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BackupJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public BackupJob startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return this.finishedAt;
    }

    public BackupJob finishedAt(Instant finishedAt) {
        this.setFinishedAt(finishedAt);
        return this;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public BackupStatus getStatus() {
        return this.status;
    }

    public BackupJob status(BackupStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BackupStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public BackupJob message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BackupFile getBackupFile() {
        return this.backupFile;
    }

    public void setBackupFile(BackupFile backupFile) {
        this.backupFile = backupFile;
    }

    public BackupJob backupFile(BackupFile backupFile) {
        this.setBackupFile(backupFile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupJob)) {
            return false;
        }
        return getId() != null && getId().equals(((BackupJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupJob{" +
            "id=" + getId() +
            ", startedAt='" + getStartedAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
