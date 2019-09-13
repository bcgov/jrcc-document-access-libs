package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import javax.validation.constraints.Min;

/**
 * Represents the rabbitmq input plugin properties
 * @author alexjoybc
 * @since 0.6.0
 *
 */
@ConfigurationProperties(prefix = "bcgov.access.input.sftp")
public class SftpInputProperties {

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
		return filterPattern == null ? "" : filterPattern ;
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

	public Resource getSshPrivateKey() {

		if(StringUtils.isBlank(this.sshPrivateKey)) return null;
		return new ByteArrayResource(this.sshPrivateKey.getBytes());
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
	 * @param knownHostFile
	 */
	public void setKnownHostFile(String knownHostFile) {
		this.knownHostFile = knownHostFile;
	}
}
