package com.example.mes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.RedisSessionRepository;

import java.time.Duration;

import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public GenericJacksonJsonRedisSerializer jsonRedisSerializer() {
        return GenericJacksonJsonRedisSerializer.builder(JsonMapper::builder)
                .enableUnsafeDefaultTyping()
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory,
                                                        GenericJacksonJsonRedisSerializer jsonRedisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          GenericJacksonJsonRedisSerializer jsonRedisSerializer) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .build();
    }

    @Bean
    public RedisSessionRepository sessionRepository(
            RedisTemplate<String, Object> redisTemplate,
            @Value("${mes.session.redis.namespace:mes:session}") String namespace,
            @Value("${mes.session.redis.flush-mode:ON_SAVE}") String flushModeStr,
            @Value("${server.servlet.session.timeout:30m}") Duration timeout) {
        RedisSessionRepository repository = new RedisSessionRepository(redisTemplate);
        repository.setRedisKeyNamespace(namespace);
        repository.setFlushMode(FlushMode.valueOf(flushModeStr.toUpperCase()));
        repository.setDefaultMaxInactiveInterval(timeout);
        return repository;
    }
}
