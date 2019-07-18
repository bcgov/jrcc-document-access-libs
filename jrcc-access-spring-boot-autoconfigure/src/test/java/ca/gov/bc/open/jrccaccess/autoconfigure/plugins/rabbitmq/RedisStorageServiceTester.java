package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;

import ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq.RedisStorageService;
import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;

public class RedisStorageServiceTester {

	
	private static final String REDIS_CONNECTION_FAILURE_EXCEPTION = "RedisConnectionFailureException";

	private static final String VALID = "valid";

	@Mock
	private CacheManager cacheManager;
	
	@Mock
	private Cache cache;

	
	private RedisStorageService sut;
	
	@Before
	public void Init() {
		
		MockitoAnnotations.initMocks(this);
	    Mockito.when(cacheManager.getCache(Mockito.anyString())).thenReturn(this.cache);
	    Mockito.doNothing().when(this.cache).put(Mockito.anyString(), Mockito.eq(VALID));
	    Mockito.doThrow(RedisConnectionFailureException.class).when(this.cache).put(Mockito.anyString(), Mockito.eq(REDIS_CONNECTION_FAILURE_EXCEPTION));
	    this.sut = new RedisStorageService(cacheManager);
	}
	
	
	@Test
	public void with_valid_content_should_return_document_properties() throws Exception {
		
		String content = VALID;
		String myHash = "9F7D0EE82B6A6CA7DDEAE841F3253059";
		
		DocumentStorageProperties result = sut.putString(content);
		
		assertNotNull(result.getKey());
		assertEquals(myHash, result.getMD5());
		
		// validation of uuid
		try {
			UUID.fromString(result.getKey());
		} catch (IllegalArgumentException e) {
			fail("key is not a valid uuid");
		}
		
		
	}
	
	
	@Test(expected = ServiceUnavailableException.class)
	public void with_RedisConnectionFailureException_should_throw_ServiceUnavailableException() throws Exception {
		
		String content = REDIS_CONNECTION_FAILURE_EXCEPTION;
		
		@SuppressWarnings("unused")
		DocumentStorageProperties result = sut.putString(content);
		
	}
	

}
