package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;

public class RedisStorageServiceTester {

	@Mock
	private StringRedisTemplate stringRedisTemplateMock;
	
	@Mock
	private ValueOperations<String, String> valueOperations;
	
	
	private RedisStorageService sut;
	
	@Before
	public void Init() {
		
//		MockitoAnnotations.initMocks(this);
//	    Mockito.when(stringRedisTemplateMock.opsForValue()).thenReturn(valueOperations);
//	    Mockito.doNothing().when(valueOperations).set(Mockito.anyString(), Mockito.anyString());	
//		this.sut = new RedisStorageService(this.stringRedisTemplateMock);	
	}
	
	
	@Test
	public void with_valid_content_should_return_document_properties() {
		
//		String content = "my content";
//		String myHash = "F2BFA7FC155C4F42CB91404198DDA01F";
//		
//		DocumentStorageProperties result = sut.putString(content);
//		
//		assertNotNull(result.getKey());
//		assertEquals(myHash, result.getMD5());
//		
//		// validation of uuid
//		try {
//			UUID.fromString(result.getKey());
//		} catch (IllegalArgumentException e) {
//			fail("key is not a valid uuid");
//		}
		
		
	}
	
	
	
	
	
}
