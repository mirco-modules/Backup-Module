package org.khasanof.backup.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.khasanof.backup.domain.common.enumeration.BackupServerStatus;
import org.khasanof.core.service.dto.base.IDto;

import java.util.Objects;

/**
 * A DTO for the {@link org.khasanof.backup.domain.common.BackupServer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupServerDTO implements IDto {

    private Long id;

    @NotNull
    private Integer port;

    @NotNull
    private BackupServerStatus status;

    @NotNull
    @Size(min = 1, max = 250)
    private String host;

    @NotNull
    @Size(min = 1, max = 250)
    private String username;

    @NotNull
    @Size(min = 1, max = 250)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public BackupServerStatus getStatus() {
        return status;
    }

    public void setStatus(BackupServerStatus status) {
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupServerDTO)) {
            return false;
        }

        BackupServerDTO backupServerDTO = (BackupServerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, backupServerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupServerDTO{" +
            "id=" + getId() +
            ", port=" + getPort() +
            ", status='" + getStatus() + "'" +
            ", host='" + getHost() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
