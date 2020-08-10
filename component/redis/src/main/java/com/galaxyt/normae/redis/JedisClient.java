package com.galaxyt.normae.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.function.Function;

/**
 * Jedis 客户端
 * @author zhouqi
 * @date 2020/5/18 13:43
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/18 13:43     zhouqi          v1.0.0           Created
 *
 */
@Component
public class JedisClient {


    @Autowired
    private JedisPool jedisPool;


    /**
     * Jedis 保存
     * @param function
     * @param <R>
     * @return
     */
    public <R> R operate(Function<Jedis, R> function) {
        Jedis jedis = this.jedisPool.getResource();
        R result = function.apply(jedis);
        jedis.close();
        return result;
    }

    /**
     * 获取一个 Redis 的链接
     * @return
     */
    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }


}
