package org.khasanof.backup.service.feature.ssh;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.khasanof.backup.config.BackupServiceProperties;
import org.khasanof.backup.domain.common.BackupServer;
import org.khasanof.backup.domain.common.BackupTenant;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

/**
 * @author Nurislom
 * @see org.khasanof.backup.service.feature.ssh
 * @since 9/5/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SshClientExecutorServiceImpl implements SshClientExecutorService {

    private final BackupServiceProperties backupServiceProperties;

    /**
     * @param tenant
     * @param command
     */
    @Override
    public void executeSshCommand(BackupTenant tenant, String command) {
        SshClient sshClient = SshClient.setUpDefaultClient();
        sshClient.start();

        BackupServer server = tenant.getServer();
        Long defaultTimeoutSeconds = backupServiceProperties.getDefaultTimeoutSeconds();

        try (ClientSession session = sshClient.connect(server.getUsername(), server.getHost(), server.getPort())
                .verify(defaultTimeoutSeconds, TimeUnit.SECONDS)
                .getSession()) {

            session.addPasswordIdentity(server.getPassword());
            session.auth().verify(backupServiceProperties.getDefaultTimeoutSeconds(), TimeUnit.SECONDS);

            try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                 ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL)) {
                channel.setOut(responseStream);
                try {
                    channel.open().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);
                    try (OutputStream pipedIn = channel.getInvertedIn()) {
                        pipedIn.write(command.getBytes());
                        pipedIn.flush();
                    }

                    channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(defaultTimeoutSeconds));
                    String responseString = responseStream.toString();
                    System.out.println(responseString);
                } finally {
                    channel.close(false);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            sshClient.stop();
        }
    }
}
