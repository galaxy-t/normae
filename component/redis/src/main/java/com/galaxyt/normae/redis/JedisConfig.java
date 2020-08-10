package com.galaxyt.normae.redis;

import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Jedis 连接池配置
 * @author zhouqi
 * @date 2020/5/18 13:40
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/18 13:40     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
@Configuration
public class JedisConfig {


    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.timeout}")
    private int redisTimeout;


    /**
     * 初始化 JedisPool
     * 使用默认配置
     * @return
     */
    @Bean
    public JedisPool getJedisPool() {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            if (StringUtils.isEmpty(this.redisPassword)) {
                return new JedisPool(jedisPoolConfig, this.redisHost, this.redisPort, this.redisTimeout);
            } else {
                return new JedisPool(jedisPoolConfig, this.redisHost, this.redisPort, this.redisTimeout, this.redisPassword);
            }
        } catch (Throwable cause) {
            log.error("Jedis Pool 初始化失败");
            cause.printStackTrace();
            throw new GlobalException(GlobalExceptionCode.JEDIS_POOL_INIT_FAIL);
        }

    }


    /*public JedisSentinelPool getJedisSentinelPool() {


        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();


        Set<String> sentinels = new HashSet<>();
        sentinels.add("127.0.0.1:26379");



        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster", sentinels, jedisPoolConfig, "");



    }
*/

    /**
     * 作为单例使用 , 因为其中内置了所有节点的连接池
     * 无需手动借还链接
     * 不支持同时处理多个key
     * @return
     */
    /*public JedisCluster getJedisCluster() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(60000);//设置最大连接数
        jedisPoolConfig.setMaxIdle(1000); //设置最大空闲数
        jedisPoolConfig.setMaxWaitMillis(3000);//设置超时时间
        jedisPoolConfig.setTestOnBorrow(true);

        // 集群结点
        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("192.168.246.128", Integer.parseInt("7001")));
        jedisClusterNode.add(new HostAndPort("192.168.246.128", Integer.parseInt("7002")));
        jedisClusterNode.add(new HostAndPort("192.168.246.128", Integer.parseInt("7003")));
        jedisClusterNode.add(new HostAndPort("192.168.246.128", Integer.parseInt("7004")));
        jedisClusterNode.add(new HostAndPort("192.168.246.128", Integer.parseInt("7005")));
        jedisClusterNode.add(new HostAndPort("192.168.246.128", Integer.parseInt("7006")));

        JedisCluster jc = new JedisCluster(jedisClusterNode, jedisPoolConfig);

        jc.set("name", "zhangsan");
        jc.get("name");

        jc.close();


    }*/



}
