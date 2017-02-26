package org.carbon.web.context.session;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

/**
 * @author Shota Oda 2016/12/17.
 */
public class RedisSessionStore implements SessionStore{
    InMemorySessionStore cache;
    Jedis jedis;
    ObjectMapper jsonMapper;

    public RedisSessionStore(String host, int port) {
        cache = new InMemorySessionStore();
        jedis = new Jedis(host, port);
        jsonMapper = new ObjectMapper();
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        T cached = cache.get(key, type);
        if (cached != null) return cached;

        String json = jedis.get(generateKey(key, type));
        if (json == null || json.isEmpty()) return null;

        cached = deserialize(json, type);
        cache.put(key, cached);

        return cached;
    }

    @Override
    public void put(String key, Object object) {
        String redisKey = generateKey(key, object.getClass());
        jedis.set(redisKey, serialize(object));
    }

    @Override
    public void remove(String key, Class type) {
        cache.remove(key, type);
        jedis.del(generateKey(key, type));
    }

    private String generateKey(String key, Class type) {
        return key + "." +  type.getSimpleName();
    }

    private String serialize(Object o) {
        try {
            return jsonMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T deserialize(String json, Class<T> type) {
        try {
            return jsonMapper.readValue(json,type);
        } catch (IOException impossible) {
            throw new RuntimeException(impossible);
        }
    }
}
