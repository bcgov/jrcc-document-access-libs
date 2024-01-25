package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.Min;
import java.util.Optional;

/**
 * Represents the rabbitmq input plugin properties
 *
 * @author alexjoybc
 * @since 0.6.0
 */
@ConfigurationProperties(prefix = "bcgov.access.input.sftp")
public class SftpInputProperties {

    private Logger logger = LoggerFactory.getLogger(SftpInputProperties.class);

    private String host;

    @Min(0)
    private Integer port;

    private String username;

    private String password;

    private String remoteDirectory;

    private String filterPattern;

    private String cron;

    private String maxMessagePerPoll;

    private String sshPrivateKey;

    private String sshPrivatePassphrase;

    private boolean allowUnknownKeys;

    private String knownHostFile;

    private Integer serverAliveInterval;

    private Integer cachingSessionWaitTimeout;

    private Integer cachingSessionMaxPoolSize;

    public String getRemoteDirectory() {
        return remoteDirectory;
    }

    public void setRemoteDirectory(String remoteDirectory) {
        this.remoteDirectory = remoteDirectory;
    }

    public String getHost() {
        return host == null ? "localhost" : host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port == null ? 22 : port;
    }

    public void setPort(String port) {
        this.port = Integer.valueOf(port);
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

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getFilterPattern() {
        return filterPattern == null ? "" : filterPattern;
    }

    public void setFilterPattern(String filterPattern) {
        this.filterPattern = filterPattern;
    }

    public String getMaxMessagePerPoll() {
        return maxMessagePerPoll;
    }

    public void setMaxMessagePerPoll(String maxMessagePerPoll) {
        this.maxMessagePerPoll = maxMessagePerPoll;
    }

    public String getSshPrivateKey() {
        logger.info("getSshPrivateKey sshPrivateKey = {}", this.sshPrivateKey);
        logger.info("getSshPrivateKey from System.getenv(SSH_PRIVATE_KEY): {}", System.getenv("SSH_PRIVATE_KEY"));
        logger.info("getSshPrivateKey from System.getenv(SFTP_HOST): {}", System.getenv("SFTP_HOST"));
        logger.info("getSshPrivateKey from System.getenv(SFTP_KNOWN_HOST_FILE): {}", System.getenv("SFTP_KNOWN_HOST_FILE"));
        if (StringUtils.isBlank(this.sshPrivateKey)) { logger.info("getSshPrivateKey return null"); return null;}
        logger.info("getSshPrivateKey return not null {}", this.sshPrivateKey);
        return this.sshPrivateKey;
    }

    public void setSshPrivateKey(String sshPrivateKey) {
        this.sshPrivateKey = sshPrivateKey;
    }

    public String getSshPrivatePassphrase() {
        return sshPrivatePassphrase;
    }

    public void setSshPrivatePassphrase(String sshPrivatePassphrase) {
        this.sshPrivatePassphrase = sshPrivatePassphrase;
    }

    /**
     * @return if allow Unknown Keys
     */
    public boolean isAllowUnknownKeys() {
        return this.allowUnknownKeys;
    }

    /**
     * Set to true to unconditionally allow connecting to an unknown host or when a host's key has changed (see knownHosts).
     * Default false. Set to true if a knownHosts file is not provided.
     *
     * @param allowUnknowKeys : true or false
     */
    public void setAllowUnknownKeys(boolean allowUnknowKeys) {
        this.allowUnknownKeys = allowUnknowKeys;
    }

    /**
     * @return the filename that will be used for a host key repository. The file has the same format as OpenSSH's known_hosts file.
     */
    public String getKnownHostFile() {
        return this.knownHostFile;
    }

    /**
     * set the known_hosts file name, including path
     *
     * @param knownHostFile
     */
    public void setKnownHostFile(String knownHostFile) {
        this.knownHostFile = knownHostFile;
    }


    public Optional<Integer> getServerAliveInterval() {
        if (this.serverAliveInterval == null) return Optional.empty();
        return Optional.of(this.serverAliveInterval);
    }

    public void setServerAliveInterval(String serverAliveInterval) {
        this.serverAliveInterval = Integer.valueOf(serverAliveInterval);
    }

    public Optional<Integer> getCachingSessionWaitTimeout() {
        return this.cachingSessionWaitTimeout == null ?
                Optional.empty() :
                Optional.of(this.cachingSessionWaitTimeout);
    }

    public void setCachingSessionWaitTimeout(String CachingSessionWaitTimeout) {
        this.cachingSessionWaitTimeout = Integer.valueOf(CachingSessionWaitTimeout);
    }

    public Optional<Integer> getCachingSessionMaxPoolSize() {
        return this.cachingSessionMaxPoolSize == null ?
                Optional.empty() :
                Optional.of(this.cachingSessionMaxPoolSize);
    }

    public void setCachingSessionMaxPoolSize(String cachingSessionMaxPoolSize) {
        this.cachingSessionMaxPoolSize = Integer.valueOf(cachingSessionMaxPoolSize);
    }

}
