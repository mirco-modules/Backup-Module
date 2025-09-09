package org.khasanof.backup.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.khasanof.backup.domain.common.enumeration.BackupStatus;
import org.khasanof.core.service.dto.base.IDto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link org.khasanof.backup.domain.common.BackupJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupJobDTO implements IDto {

    private Long id;

    @NotNull
    private Instant startedAt;

    private Instant finishedAt;

    @NotNull
    private BackupStatus status;

    @Size(max = 1000)
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public BackupStatus getStatus() {
        return status;
    }

    public void setStatus(BackupStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupJobDTO)) {
            return false;
        }

        BackupJobDTO backupJobDTO = (BackupJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, backupJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupJobDTO{" +
            "id=" + getId() +
            ", startedAt='" + getStartedAt() + "'" +
            ", finishedAt='" + getFinishedAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
