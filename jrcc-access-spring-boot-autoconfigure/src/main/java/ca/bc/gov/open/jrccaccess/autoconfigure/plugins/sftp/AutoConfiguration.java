package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.InvalidConfigException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.KnownHostFileNotDefinedException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.KnownHostFileNotFoundException;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

import jakarta.websocket.MessageHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

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
        logger.info("SFTP Configuration: Port => [{}]", this.properties.getPort());
        logger.info("SFTP Configuration: Username => [{}]", this.properties.getUsername());
        logger.info("SFTP Configuration: Remote Directory => [{}]", this.properties.getRemoteDirectory());
        logger.info("SFTP Configuration: Filter Pattern => [{}]", this.properties.getFilterPattern());
        logger.info("SFTP Configuration: Cron => [{}]", this.properties.getCron());
        logger.info("SFTP Configuration: Max Message Per Poll => [{}]", this.properties.getMaxMessagePerPoll());
        logger.info("SFTP Configuration: Known Host File => [{}]", this.properties.getKnownHostFile());
    }

    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory() throws InvalidConfigException, IOException {

        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setUser(properties.getUsername());
        if (properties.getSshPrivateKey() != null) {
            if(!(new File(properties.getSshPrivateKey()).exists()))
                throw new KnownHostFileNotDefinedException("Cannot find known_hosts file private key file. ");

            logger.info("SFTP Configuration: setPrivateKey");
            Resource resource = resourceLoader.getResource("file:"+properties.getSshPrivateKey());;
            logger.info("SFTP Configuration: privateKey - length {}", resource.contentLength());
            logger.info("SFTP Configuration: privateKey - content {}", resource.getContentAsString(Charset.defaultCharset()));
            factory.setPrivateKey(resource);

            factory.setPrivateKeyPassphrase(properties.getSshPrivatePassphrase());
        } else {
            logger.info("SFTP Configuration: setPassword");
            factory.setPassword(properties.getPassword());
        }
        boolean isAllowUnknownKeys = properties.isAllowUnknownKeys();
        factory.setAllowUnknownKeys(isAllowUnknownKeys);
        if (!isAllowUnknownKeys) {
            String knownHostFileStr = properties.getKnownHostFile();
            if (StringUtils.isBlank(knownHostFileStr))
                throw new KnownHostFileNotDefinedException("Must define known_hosts file when allow-unknown-keys is false. ");

            File knownHostFile = new File(knownHostFileStr);
            if (!knownHostFile.exists())
                throw new KnownHostFileNotFoundException("Cannot find known_hosts file when allow-unknown-keys is false.");

            logger.info("SFTP Known Hosts");
            Resource resource = resourceLoader.getResource("file:"+properties.getKnownHostFile());
            logger.info("SFTP Known Hosts: length = {}", resource.contentLength());
            logger.info("SFTP Known Hosts: content = {}", resource.getContentAsString(Charset.defaultCharset()));
            factory.setKnownHostsResource(resource);
        }

        CachingSessionFactory<SftpClient.DirEntry> cachingSessionFactory = new CachingSessionFactory<>(factory);
        this.properties.getServerAliveInterval().ifPresent(timeout -> cachingSessionFactory.setSessionWaitTimeout(timeout));
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
