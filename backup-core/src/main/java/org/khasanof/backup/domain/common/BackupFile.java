package org.khasanof.backup.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.khasanof.core.annotation.Common;
import org.khasanof.core.domain.AbstractAuditingEntity;

import java.time.Instant;

/**
 * A BackupFile.
 */
@Entity
@Common
@Table(name = "backup_file")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupFile extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @NotNull
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "backupFile" }, allowSetters = true)
    private BackupJob backupJob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "setting", "backupFiles" }, allowSetters = true)
    private BackupTenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BackupFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public BackupFile filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public BackupFile fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public BackupFile createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BackupJob getBackupJob() {
        return this.backupJob;
    }

    public void setBackupJob(BackupJob backupJob) {
        this.backupJob = backupJob;
    }

    public BackupFile backupJob(BackupJob backupJob) {
        this.setBackupJob(backupJob);
        return this;
    }

    public BackupTenant getTenant() {
        return this.tenant;
    }

    public void setTenant(BackupTenant backupTenant) {
        this.tenant = backupTenant;
    }

    public BackupFile tenant(BackupTenant backupTenant) {
        this.setTenant(backupTenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupFile)) {
            return false;
        }
        return getId() != null && getId().equals(((BackupFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupFile{" +
            "id=" + getId() +
            ", filePath='" + getFilePath() + "'" +
            ", fileSize=" + getFileSize() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
