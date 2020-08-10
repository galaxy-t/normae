package com.galaxyt.normae.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Jedis 缓存
 * @author zhouqi
 * @date 2020/5/18 14:05
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/18 14:05     zhouqi          v1.0.0           Created
 *
 */
@Component
public class JedisCache {


    @Autowired
    private JedisClient jedisClient;

    /**
     * gson 对象
     */
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    /**
     * 从缓存中获取 key 所对应的结果
     * 若缓存中不存在则执行 supplier 函数 , 并将其结果缓存
     * 缓存结果永不过期
     * @param key 缓存中的 key
     * @param c   期望得到的类型
     * @param supplier  若缓存中不存在则会执行该函数
     * @param <R>
     * @return
     */
    public <R> R able(final String key, Class c, Supplier<R> supplier) {
        return this.able(key, c, 0, supplier);
    }


    /**
     * 从缓存中获取 key 所对应的结果
     * 若缓存中不存在则执行 supplier 函数 , 并将其结果缓存
     * 并设置过期时间
     * @param key   缓存中的 key
     * @param c     期望得到的类型
     * @param timeout       过期时间 单位/秒
     * @param supplier      若缓存中不存在则会执行该函数
     * @param <R>
     * @return
     */
    public <R> R able(final String key, Class c, final int timeout, Supplier<R> supplier) {

        //从 redis 中获取 key 所对应的 value
        String value = this.jedisClient.operate(jedis -> jedis.get(key));

        //将 value 包装成 Optional 对象
        Optional<String> valueO = Optional.ofNullable(value);

        //初始化返回值
        R r = null;

        //如果能从 redis 中获取到值 , 则将其进行类型转换
        if (valueO.isPresent()) {
            r = (R) this.gson.fromJson(value, c);
        } else {    //若获取不到则执行查询
            //查询
            r = supplier.get();
            //将查询结果转换成 Gson
            final String newValue = this.gson.toJson(r);
            //缓存到 redis 中
            this.set(key, newValue, timeout);
        }

        return r;
    }

    /**
     * 不从缓存中获取结果
     * 每次都会执行 supplier 函数 , 并将其结果缓存到 redis 中
     * 缓存结果永不过期
     * @param key       缓存中的 key
     * @param supplier  要被执行的函数
     * @param <R>       期望得到的返回值
     * @return
     */
    public <R> R put(final String key, Supplier<R> supplier) {
        return this.put(key, 0, supplier);
    }

    /**
     * 不从缓存中获取结果
     * 每次都会执行 supplier 函数 , 并将其结果缓存到 redis 中
     *
     * @param key       缓存中的 key
     * @param timeout   要被执行的函数
     * @param supplier  过期时间  单位/秒
     * @param <R>       期望得到的返回值
     * @return
     */
    public <R> R put(final String key, final int timeout, Supplier<R> supplier) {
        R r = supplier.get();
        final String value = this.gson.toJson(r);
        this.set(key, value, timeout);
        return r;
    }

    /**
     * 删除一个缓存的 key
     * @param key
     */
    public void evict(final String key) {
        this.jedisClient.operate(jedis -> jedis.del(key));
    }


    /**
     * 设置新的值
     * @param key
     * @param value     仅接受字符串
     * @param timeout   单位/秒    大于 0 则代表有过期时间 , 小于等于 0 则代表无过期时间
     */
    private void set(final String key, final String value, final int timeout) {

        this.jedisClient.operate(jedis -> {
            if (timeout <= 0) {
                return jedis.set(key, value);
            } else {
                return jedis.setex(key, timeout, value);
            }
        });

    }



}
