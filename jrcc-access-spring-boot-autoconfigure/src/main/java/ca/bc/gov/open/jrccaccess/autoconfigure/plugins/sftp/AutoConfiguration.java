package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import com.jcraft.jsch.ChannelSftp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptAllFileListFilter;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.filters.SftpRegexPatternFileListFilter;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.inbound.SftpStreamingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

import javax.websocket.MessageHandler;
import java.io.File;
import java.io.InputStream;

@Configuration
@ComponentScan
@EnableConfigurationProperties(SftpInputProperties.class)
@ConditionalOnProperty(
        value="bcgov.access.input.plugin",
        havingValue = "sftp"
)
public class AutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(AutoConfiguration.class);

    private SftpInputProperties properties;

    public AutoConfiguration(SftpInputProperties sftpInputProperties) {
        this.properties = sftpInputProperties;

        logger.debug("SFTP Configuration: Host => [{}]", this.properties.getHost());
        logger.debug("SFTP Configuration: Port => [{}]", this.properties.getPort());

    }


    @Bean
    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setUser(properties.getUsername());
        factory.setPassword(properties.getPassword());
        factory.setAllowUnknownKeys(true);
        return new CachingSessionFactory<ChannelSftp.LsEntry>(factory);
    }

    @Bean
    public SftpRemoteFileTemplate template() {
        SftpRemoteFileTemplate sftpRemoteFileTemplate = new SftpRemoteFileTemplate(sftpSessionFactory());
        return sftpRemoteFileTemplate;
    }

    @Bean
    @InboundChannelAdapter(channel = "sftpChannel", poller = @Poller(cron = "${bcgov.access.input.sftp.cron}", maxMessagesPerPoll = "${bcgov.access.input.sftp.max-message-per-poll}"))
    public MessageSource<InputStream> sftpMessageSource() {
        SftpStreamingMessageSource messageSource = new SftpStreamingMessageSource(template());
        messageSource.setRemoteDirectory(properties.getRemoteDirectory());
        if(properties.getFilterPattern() != null && !"".equals(properties.getFilterPattern()))
            messageSource.setFilter(new SftpRegexPatternFileListFilter(properties.getFilterPattern()));
        return messageSource;
    }

    @Bean
    @ServiceActivator(inputChannel = "sftpChannel")
    public MessageHandler handler(SftpDocumentInput sftpDocumentInput) {
        return sftpDocumentInput;
    }


}
