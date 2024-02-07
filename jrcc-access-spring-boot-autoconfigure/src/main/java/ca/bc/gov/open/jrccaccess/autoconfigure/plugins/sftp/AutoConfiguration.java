package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.InvalidConfigException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.KnownHostFileNotDefinedException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.KnownHostFileNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.client.ClientBuilder;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.auth.password.PasswordIdentityProvider;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.keyverifier.RejectAllServerKeyVerifier;
import org.apache.sshd.client.keyverifier.ServerKeyVerifier;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.config.keys.FilePasswordProvider;
import org.apache.sshd.common.kex.BuiltinDHFactories;
import org.apache.sshd.common.keyprovider.KeyIdentityProvider;
import org.apache.sshd.common.signature.BuiltinSignatures;
import org.apache.sshd.common.util.io.resource.AbstractIoResource;
import org.apache.sshd.common.util.io.resource.IoResource;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.sftp.client.SftpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.ChainFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.metadata.ConcurrentMetadataStore;
import org.springframework.integration.sftp.filters.SftpPersistentAcceptOnceFileListFilter;
import org.springframework.integration.sftp.filters.SftpRegexPatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpStreamingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.ResourceKnownHostsServerKeyVerifier;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

import jakarta.websocket.MessageHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
@ComponentScan
@EnableConfigurationProperties(SftpInputProperties.class)
@ConditionalOnProperty(
        value = "bcgov.access.input.plugin",
        havingValue = "sftp"
)
public class AutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(AutoConfiguration.class);
    @Autowired
    ResourceLoader resourceLoader;

    private SftpInputProperties properties;

    public AutoConfiguration(SftpInputProperties sftpInputProperties) {
        this.properties = sftpInputProperties;
        logger.info("SFTP Configuration: Host => [{}]", this.properties.getHost());
        logger.debug("SFTP Configuration: Port => [{}]", this.properties.getPort());
        logger.debug("SFTP Configuration: Username => [{}]", this.properties.getUsername());
        logger.debug("SFTP Configuration: Remote Directory => [{}]", this.properties.getRemoteDirectory());
        logger.debug("SFTP Configuration: Filter Pattern => [{}]", this.properties.getFilterPattern());
        logger.debug("SFTP Configuration: Cron => [{}]", this.properties.getCron());
        logger.debug("SFTP Configuration: Max Message Per Poll => [{}]", this.properties.getMaxMessagePerPoll());
        logger.debug("SFTP Configuration: Known Host File => [{}]", this.properties.getKnownHostFile());
    }

    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory() throws InvalidConfigException, IOException {

        SshClient sshClient = SshClient.setUpDefaultClient();
        sshClient.setKeyExchangeFactories(NamedFactory.setUpTransformedFactories(
                false,
                BuiltinDHFactories.VALUES,
                ClientBuilder.DH2KEX
        ));
        sshClient.setSignatureFactories(new ArrayList<>(BuiltinSignatures.VALUES));

        boolean isAllowUnknownKeys = properties.isAllowUnknownKeys();
        ServerKeyVerifier serverKeyVerifier =
                isAllowUnknownKeys ? AcceptAllServerKeyVerifier.INSTANCE : RejectAllServerKeyVerifier.INSTANCE;

        if (!isAllowUnknownKeys) {
            String knownHostFileStr = properties.getKnownHostFile();
            if (StringUtils.isBlank(knownHostFileStr))
                throw new KnownHostFileNotDefinedException("Must define known_hosts file when allow-unknown-keys is false. ");

            File knownHostFile = new File(knownHostFileStr);
            if (!knownHostFile.exists())
                throw new KnownHostFileNotFoundException("Cannot find known_hosts file when allow-unknown-keys is false.");

            Resource resource = resourceLoader.getResource("file:"+properties.getKnownHostFile());
            serverKeyVerifier = new ResourceKnownHostsServerKeyVerifier(resource);
        }

        sshClient.setServerKeyVerifier(serverKeyVerifier);
        sshClient.setPasswordIdentityProvider(PasswordIdentityProvider.wrapPasswords(properties.getPassword()));

        if (properties.getSshPrivateKey() != null) {
            if(!(new File(properties.getSshPrivateKey()).exists()))
                throw new KnownHostFileNotDefinedException("Cannot find known_hosts file private key file. ");

            Resource resource = resourceLoader.getResource("file:"+properties.getSshPrivateKey());;

            IoResource<Resource> privateKeyResource =
                    new AbstractIoResource<>(Resource.class, resource) {

                        @Override
                        public InputStream openInputStream() throws IOException {
                            return getResourceValue().getInputStream();
                        }

                    };
            try {
                Collection<KeyPair> keys =
                        SecurityUtils.getKeyPairResourceParser()
                                .loadKeyPairs(null, privateKeyResource,
                                        FilePasswordProvider.of(properties.getSshPrivatePassphrase()));
                sshClient.setKeyIdentityProvider(KeyIdentityProvider.wrapKeyPairs(keys));
            }
            catch (GeneralSecurityException ex) {
                throw new IOException("Cannot load private key: " + resource.getFilename(), ex);
            }
        }

        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(sshClient, true);

        factory.setHost(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setUser(properties.getUsername());

        CachingSessionFactory<SftpClient.DirEntry> cachingSessionFactory = new CachingSessionFactory<>(factory);
        this.properties.getServerAliveInterval().ifPresent(timeout -> cachingSessionFactory.setTestSession(false));
        return cachingSessionFactory;
    }

    @Bean
    public SftpRemoteFileTemplate template() {
        try {
            return new SftpRemoteFileTemplate(sftpSessionFactory());
        } catch (InvalidConfigException ex) {
            logger.error(ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Bean
    @InboundChannelAdapter(channel = "sftpChannel", poller = @Poller(cron = "${bcgov.access.input.sftp.cron}", maxMessagesPerPoll = "${bcgov.access.input.sftp.max-message-per-poll}"))
    public MessageSource<InputStream> sftpMessageSource(ConcurrentMetadataStore concurrentMetadataStore) {
        logger.info("InboundChannelAdapter started...");
        ChainFileListFilter<SftpClient.DirEntry> filterChain = new ChainFileListFilter<>();
        if (properties.getFilterPattern() != null && !"".equals(properties.getFilterPattern()))
            filterChain.addFilter(new SftpRegexPatternFileListFilter(properties.getFilterPattern()));
        filterChain.addFilter(new SftpPersistentAcceptOnceFileListFilter(concurrentMetadataStore, "sftpSource"));
        SftpStreamingMessageSource messageSource = new SftpStreamingMessageSource(template());
        messageSource.setRemoteDirectory(properties.getRemoteDirectory());
        messageSource.setFilter(filterChain);

        return messageSource;
    }

    @Bean
    @ServiceActivator(inputChannel = "sftpChannel")
    public MessageHandler handler(SftpDocumentInput sftpDocumentInput) {
        return sftpDocumentInput;
    }

}
