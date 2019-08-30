package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.bc.gov.open.jrccaccess.libs.DocumentStorageProperties;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentDigestMatchFailedException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentNotFoundException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;

import java.util.UUID;

import static org.junit.Assert.*;

public class RedisStorageServiceTests {

	private static final String VALID = "valid";
	private static final String HASH = "9F7D0EE82B6A6CA7DDEAE841F3253059";
	private static final String KEY = "key";
	private static final String MISSING_DOCUMENT = "MISSING_DOCUMENT";
	private static final String REDIS_CONNECTION_FAILURE_EXCEPTION = "RedisConnectionFailureException";

	@Mock
	private CacheManager cacheManager;
	
	@Mock
	private Cache cache;

	@Mock
	private AccessProperties accessProperties;
	
	@Mock
	private PluginConfig pluginConfig;
	
	@Mock
	private ValueWrapper valueWrapper;
	
	private RedisStorageService sut;
	
	@Before
	public void Init() {
		
		MockitoAnnotations.initMocks(this);
		Mockito.when(valueWrapper.get()).thenReturn(VALID);
	    Mockito.when(cacheManager.getCache("test-doc")).thenReturn(this.cache);
	    Mockito.when(cache.get(KEY)).thenReturn(valueWrapper);
	    Mockito.when(cache.get(MISSING_DOCUMENT)).thenReturn(null);
	    Mockito.doNothing().when(this.cache).put(Mockito.anyString(), Mockito.eq(VALID));
	    Mockito.doThrow(RedisConnectionFailureException.class).when(this.cache).put(Mockito.anyString(), Mockito.eq(REDIS_CONNECTION_FAILURE_EXCEPTION));
	    Mockito.doThrow(RedisConnectionFailureException.class).when(this.cache).get(Mockito.eq(REDIS_CONNECTION_FAILURE_EXCEPTION));
	    Mockito.when(pluginConfig.getDocumentType()).thenReturn("test-doc");
	    Mockito.when(accessProperties.getInput()).thenReturn(pluginConfig);
	    Mockito.when(accessProperties.getOutput()).thenReturn(pluginConfig);
	    this.sut = new RedisStorageService(cacheManager, accessProperties);
	}
	
	
	@Test
	public void with_valid_content_should_return_document_properties() throws Exception {
		
		DocumentStorageProperties result = sut.putString(VALID);
		
		assertNotNull(result.getKey());
		assertEquals(HASH, result.getDigest());
		
		// validation of uuid
		try {
			UUID.fromString(result.getKey());
		} catch (IllegalArgumentException e) {
			fail("key is not a valid uuid");
		}

	}
	
	@Test(expected = ServiceUnavailableException.class)
	public void with_RedisConnectionFailureException_should_throw_ServiceUnavailableException() throws Exception {
		
		@SuppressWarnings("unused")
		DocumentStorageProperties result = sut.putString(REDIS_CONNECTION_FAILURE_EXCEPTION);
		
	}
	
	@Test
	public void getString_with_valid_input_should_return_string() throws DocumentMessageException {
		
		String result = sut.getString(KEY, HASH);
		
		assertEquals("valid", result);
		
	}
	
	@Test(expected = DocumentDigestMatchFailedException.class)
	public void getString_with_invalid_digest_should_throw_DocumentDigestMatchFailedException() throws DocumentMessageException {
		@SuppressWarnings("unused")
		String result = sut.getString(KEY, "098A");
	}
	

	@Test(expected = ServiceUnavailableException.class)
	public void getString_with_invalid_digest_should_throw_ServiceUnavailableException() throws DocumentMessageException {
		@SuppressWarnings("unused")
		String result = sut.getString(REDIS_CONNECTION_FAILURE_EXCEPTION, "098A");
	}

	@Test(expected = DocumentNotFoundException.class)
	public void getString_with_missing_document_should_throw_DocumentNotFoundException() throws DocumentMessageException {
		@SuppressWarnings("unused")
		String expected = sut.getString(MISSING_DOCUMENT, "098A");
	}
	
}
