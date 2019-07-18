package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.StorageService;
import ca.gov.bc.open.jrccaccess.libs.services.exceptions.DocumentDigestMatchFailedException;
import ca.gov.bc.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import ca.gov.bc.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import ca.gov.bc.open.jrccaccess.libs.utils.DigestUtils;

/**
 * The redisStorageService provides services to interact with Redis cache.
 * 
 * @author ajoyeux
 * @since 0.1.0
 *
 */
@Service
@ConditionalOnExpression("'${bcgov.access.input.plugin}' == 'rabbitmq' || '${bcgov.access.output.plugin}' == 'rabbitmq'")
public class RedisStorageService implements StorageService {

	CacheManager cacheManager;
	
	/**
	 * Default constructor
	 * @param stringRedisTemplate a string redis template
	 */
	public RedisStorageService(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	
	/**
	 * Store the content in redis cache using a new guid as key
	 */
	@Override
	public DocumentStorageProperties putString(String content) throws DocumentMessageException {

		String key = UUID.randomUUID().toString();
		String md5Hash = DigestUtils.computeMd5(content);
		
		try {
			this.cacheManager.getCache("Document").put(key, content);
			return new DocumentStorageProperties(key, md5Hash);
		} catch (RedisConnectionFailureException e) {
			throw new ServiceUnavailableException("redis service unavailable", e.getCause());
		}

	}
	
	@Override
	public String getString(String key, String digest) throws DocumentMessageException {


		try {
			ValueWrapper valueWrapper = this.cacheManager.getCache("Document").get(key);
			String content = (String) valueWrapper.get();
			String digestToCompare = DigestUtils.computeMd5(content);
			
			if(digestToCompare.equals(digest)) {
				return content;
			} else {
				throw new DocumentDigestMatchFailedException("Document digest failed comparison: Key=" + key);
			}

		} catch (RedisConnectionFailureException e) {
			throw new ServiceUnavailableException("redis service unavailable", e.getCause());
		}

	}

}
