package ca.bc.gov.open.jrccaccess.autoconfigure.redis;

import ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq.RabbitMqOutputProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.integration.metadata.ConcurrentMetadataStore;

public class AutoConfigurationTests {

    @Mock
    private RedisProperties redisProperties;

    private AutoConfiguration autoConfiguration;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        autoConfiguration = new AutoConfiguration();
        redisProperties = Mockito.mock(RedisProperties.class);
    }

    @Test
    public void stand_alone_input_should_generate_jedisConnectionFactory() {
        Mockito.when(redisProperties.getHost()).thenReturn("127.0.0.1");
        Mockito.when(redisProperties.getPort()).thenReturn(6379);
        Mockito.when(redisProperties.getPassword()).thenReturn("password");
        JedisConnectionFactory jedisConnectionFactory = autoConfiguration.jedisConnectionFactory(redisProperties);
        Assert.assertNotNull(jedisConnectionFactory);
    }

    @Test
    public void cluster_input_should_generate_jedisConnectionFactory() {
        RedisProperties.Cluster cluster = Mockito.mock(RedisProperties.Cluster.class);
        Mockito.when(redisProperties.getCluster()).thenReturn(cluster);
        Mockito.when(redisProperties.getPassword()).thenReturn("password");
        JedisConnectionFactory jedisConnectionFactory = autoConfiguration.jedisConnectionFactory(redisProperties);
        Assert.assertNotNull(jedisConnectionFactory);
    }

    @Test
    public void sentinel_input_should_generate_jedisConnectionFactory() {
        RedisProperties.Sentinel sentinel = Mockito.mock(RedisProperties.Sentinel.class);
        Mockito.when(sentinel.getMaster()).thenReturn("master");
        Mockito.when(redisProperties.getSentinel()).thenReturn(sentinel);
        Mockito.when(redisProperties.getPassword()).thenReturn("password");
        JedisConnectionFactory jedisConnectionFactory = autoConfiguration.jedisConnectionFactory(redisProperties);
        Assert.assertNotNull(jedisConnectionFactory);
    }

    @Test
    public void correct_input_should_generate_jedisConnectionFactory() {
        Mockito.when(redisProperties.getHost()).thenReturn("127.0.0.1");
        Mockito.when(redisProperties.getPort()).thenReturn(6379);
        Mockito.when(redisProperties.getPassword()).thenReturn("password");
        JedisConnectionFactory jedisConnectionFactory = autoConfiguration.jedisConnectionFactory(redisProperties);
        Assert.assertNotNull(jedisConnectionFactory);
    }

    @Test
    public void correct_input_should_return_cacheManager() {
        RabbitMqOutputProperties rabbitMqOutputProperties = Mockito.mock(RabbitMqOutputProperties.class);
        JedisConnectionFactory jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);
        CacheManager cacheManager = autoConfiguration.cacheManager(jedisConnectionFactory, rabbitMqOutputProperties);
        Assert.assertNotNull(cacheManager);
    }

    @Test
    public void correct_input_should_return_ConcurrentMetadataStore() {
        JedisConnectionFactory jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);
        ConcurrentMetadataStore metadataStore = autoConfiguration.redisMetadataStore(jedisConnectionFactory);
        Assert.assertNotNull(metadataStore);
    }
}