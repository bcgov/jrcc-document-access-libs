package ca.gov.bc.open.jrccaccess.autoconfigure;

import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bcgov.access")
public class AccessProperties {
	
	@Min(0)
	private Integer ttl;

	public Integer getTtl() {
		return this.ttl == null ? 1 : this.ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = Integer.decode(ttl);
	}

	
}
