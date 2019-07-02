package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.StorageService;

/**
 * Redis implementation of storage service
 * 
 * @author ajoyeux
 *
 */
@Service
public class RedisStorageService implements StorageService {


	StringRedisTemplate stringRedisTemplate;
	
	/**
	 * Default constructor
	 * @param stringRedisTemplate a string redis template
	 */
	public RedisStorageService(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}
	
	
	/**
	 * Store the content in redis cache using a new guid as key
	 */
	@Override
	public DocumentStorageProperties putString(String content) {

		String key = UUID.randomUUID().toString();
		String md5Hash = computeMd5(content);

		ValueOperations<String, String> valueOperations = this.stringRedisTemplate.opsForValue();

		valueOperations.set(key, content);

		return new DocumentStorageProperties(key, md5Hash);

	}

	private String computeMd5(String content) {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(content.getBytes(StandardCharsets.UTF_8));
			return DatatypeConverter.printHexBinary(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// can't happen
			e.printStackTrace();
			return "";
		}

	}

}
