package cache.strategy.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@DataRedisTest
public class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @DisplayName("RedisTemplate에서 Lettuce Connection은 재사용되지 않는다.")
    @Test
    void test() {
        // given
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();

        RedisConnection connection1 = connectionFactory.getConnection();
        System.out.println("첫 번째 Connection: " + connection1);

        RedisConnection connection2 = connectionFactory.getConnection();
        System.out.println("두 번째 Connection: " + connection2);

        // when then
        System.out.println("같은 LettuceConnection인가? " + (connection1 == connection2));
        Assertions.assertThat(connection1).isNotSameAs(connection2);
    }

    @DisplayName("RedisTemplate에서 Native Connection은 재사용된다.")
    @Test
    void test1() {
        // given
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();

        Object nativeConnection1 = connectionFactory.getConnection().getNativeConnection();
        System.out.println("첫 번째 Native Connection: " + nativeConnection1);

        Object nativeConnection2 = connectionFactory.getConnection().getNativeConnection();
        System.out.println("두 번째 Native Connection: " + nativeConnection2);

        // when then
        System.out.println("같은 Native Connection인가? " + (nativeConnection1 == nativeConnection2));
        Assertions.assertThat(nativeConnection1).isSameAs(nativeConnection2);
    }
}
