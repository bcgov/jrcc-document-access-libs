package ca.bc.gov.open.jrccaccess.autoconfigure.redis;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import ca.bc.gov.open.jrccaccess.libs.DocumentStorageProperties;
import ca.bc.gov.open.jrccaccess.libs.StorageService;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentDigestMatchFailedException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentNotFoundException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import ca.bc.gov.open.jrccaccess.libs.utils.DigestUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.UUID;

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

	private final CacheManager cacheManager;
	private final AccessProperties accessProperties;
	private static final String serviceUnavailableMessage = "redis service unavailable";

	/**
	 * Default constructor
	 * 
	 * @param cacheManager a spring cache manager
	 * @param accessProperties document access properties
	 */
	public RedisStorageService(CacheManager cacheManager, AccessProperties accessProperties) {
		this.cacheManager = cacheManager;
		this.accessProperties = accessProperties;
	}

	/**
	 * Store the content in redis cache using a new guid as key
	 */
	@Override
	public DocumentStorageProperties putString(String content) throws DocumentMessageException {

		String key = UUID.randomUUID().toString();
		String md5Hash = DigestUtils.computeMd5(content);

		try {
			this.cacheManager.getCache(accessProperties.getOutput().getDocumentType()).put(key, content);
			return new DocumentStorageProperties(key, md5Hash);
		} catch (RedisConnectionFailureException e) {
			throw new ServiceUnavailableException(serviceUnavailableMessage, e.getCause());
		}

	}

    /**
     * Gets a document from redis key value store.
     * @param key    object key to retrieve from storage
     * @param digest MD5 Digest
     * @return
     * @throws DocumentMessageException
     */
	@Override
	public String getString(String key, String digest) throws DocumentMessageException {

		try {

			ValueWrapper valueWrapper = this.cacheManager
                    .getCache(accessProperties.getInput().getDocumentType())
					.get(key);

			if(valueWrapper == null)
			    throw new DocumentNotFoundException(MessageFormat.format("document [{0}] not found.", key));

			String content = (String) valueWrapper.get();

			if (!DigestUtils.computeMd5(content).equals(digest))
			    throw new DocumentDigestMatchFailedException("Document digest failed comparison: Key=" + key);

            return content;

		} catch (RedisConnectionFailureException e) {
			throw new ServiceUnavailableException(serviceUnavailableMessage, e.getCause());
		}

	}

	/**
	 * Deletes a key/document pair from redis store
	 * @param key
	 * @return
	 */
	public Boolean deleteString(String key) throws DocumentMessageException {

		try {
			this.cacheManager.getCache(accessProperties.getInput().getDocumentType()).evict(key);
			return true;
		} catch (RedisConnectionFailureException e) {
			throw new DocumentMessageException(serviceUnavailableMessage, e.getCause());
		}

	}
}
