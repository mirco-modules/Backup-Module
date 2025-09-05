package org.khasanof.backup.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.khasanof.backup.domain.common.enumeration.BackupServerStatus;
import org.khasanof.core.domain.AbstractAuditingEntity;

import java.io.Serializable;

/**
 * A BackupServer.
 */
@Entity
@Table(name = "backup_server")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BackupServer extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "port", nullable = false)
    private Integer port;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BackupServerStatus status;

    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "host", length = 250, nullable = false)
    private String host;

    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "username", length = 250, nullable = false)
    private String username;

    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "password", length = 250, nullable = false)
    private String password;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BackupServer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPort() {
        return this.port;
    }

    public BackupServer port(Integer port) {
        this.setPort(port);
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public BackupServerStatus getStatus() {
        return this.status;
    }

    public BackupServer status(BackupServerStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BackupServerStatus status) {
        this.status = status;
    }

    public String getHost() {
        return this.host;
    }

    public BackupServer host(String host) {
        this.setHost(host);
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return this.username;
    }

    public BackupServer username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public BackupServer password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BackupServer)) {
            return false;
        }
        return getId() != null && getId().equals(((BackupServer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BackupServer{" +
            "id=" + getId() +
            ", port=" + getPort() +
            ", status='" + getStatus() + "'" +
            ", host='" + getHost() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
