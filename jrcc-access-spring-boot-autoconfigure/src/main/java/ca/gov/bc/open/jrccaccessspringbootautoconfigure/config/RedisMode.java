package ca.gov.bc.open.jrccaccessspringbootautoconfigure.config;

public enum RedisMode {

	STANDALONE("standalone"),
	CLUSTER("cluster"),
	SANTINEL("santinel");
	
	private String mode;

	RedisMode(String mode) {
		this.mode = mode;
	}

	public String getUrl() {
		return mode;
	}
	
}
