package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;

/**
 * Represents the rabbitmq output plugin properties
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

	private String localDirectory;

	private String filterPattern;

	private String cron;

	public String getRemoteDirectory() {
		return remoteDirectory;
	}

	public void setRemoteDirectory(String remoteDirectory) {
		this.remoteDirectory = remoteDirectory;
	}

	public String getLocalDirectory() {
		return localDirectory;
	}

	public void setLocalDirectory(String localDirectory) {
		this.localDirectory = localDirectory;
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
}
