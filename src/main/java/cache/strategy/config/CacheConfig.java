package cache.strategy.config;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@EnableCaching
@Configuration
public class CacheConfig {

    /**
     * RedisCacheConfiguration 의 기본 설정 값들(defaultCacheConfig)
     *      key expiration: eternal
     *      cache null values: yes
     *      prefix cache keys: yes
     *      default prefix: [the actual cache name]
     *      key serializer: org.springframework.data.redis.serializer.StringRedisSerializer
     *      value serializer: org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
     *      conversion service: DefaultFormattingConversionService with default cache key converters
     */
    @Bean
    public CacheManager cacheConfiguration(RedisConnectionFactory redisConnectionFactoryDB1) {
        RedisCacheConfiguration memberConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1))
                .disableCachingNullValues()
                .serializeValuesWith(
                        SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())); //JSON 으로 저장 가능

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactoryDB1) // 어떤 Redis Client 쓸건지
                .withCacheConfiguration("Member", memberConfig) // 어떤 CacheName에 어떤 Config 를 적용할건지
                .build();
    }

    /**
     * Redis의 인덱스 설정을 통해 특정 Config를 여러 DB에
     * 1개의 Redis Node 의 Client은 RedisConnectionFactory 정의
     * RedisStandaloneConfiguration 은 기본 값이 localhost, port 이므로 배포 시 설정 변경 해야 함.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactoryDB1() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(1);
        return new LettuceConnectionFactory(configuration);
    }
}
