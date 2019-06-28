package ca.gov.bc.open.jrccaccessspringbootautoconfigure.config;


import java.util.Arrays;
import java.util.Collection;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * Represents the redis properties
 * 
 * @author alexjoybc
 *
 */
@ConfigurationProperties(prefix = "bcgov.access.redis")
public class RedisProperties {

	private String host;
	
	@Min(1025)
	@Max(65536)
	private Integer port;
	
	private RedisMode mode;
	
	private Collection<String> clusterHostAndPort;

	/**
	 * Gets the redis host
	 * @return The redis host
	 */
	public String getHost() {
		return this.host == null ? "localhost" : this.host;
	}

	/**
	 * Sets the redis host
	 * @param host The redis host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Gets the redis port
	 * @return The redis port.
	 */
	public Integer getPort() {
		return this.port == null ? 6379 : this.port;
	}

	/**
	 * Sets the redis port
	 * @param port The redis port
	 */
	public void setPort(String port) {
		this.port = Integer.decode(port);
	}

	public RedisMode getMode() {
		return this.mode == null ? RedisMode.STANDALONE : this.mode;
	}

	public void setMode(String mode) {
		try {
			this.mode = RedisMode.valueOf(mode.toUpperCase());
		} catch (IllegalArgumentException ex) {
			this.mode = RedisMode.STANDALONE;
		} 
	}

	public Collection<String> getClusterHostAndPort() {
		return clusterHostAndPort;
	}

	public void setClusterHostAndPort(String clusterHostAndPort) {
		this.clusterHostAndPort = Arrays.asList(clusterHostAndPort.split(","));
	}

	
	
}
