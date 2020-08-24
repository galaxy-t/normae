package com.galaxyt.normae.redis;


import redis.clients.jedis.Jedis;

/**
 * 基于 redis 实现的分布式锁
 * @author zhouqi
 * @date 2020/4/10 15:33
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
 ---------------------------------------------------------------------------------*
 * 2020/4/10 15:33     zhouqi          v1.0.0           Created
 *
 */
public class JedisLock {

    /**
     * 链接
     */
    private Jedis jedis;

    /**
     * 锁的 key
     */
    private String lockKey;

    /**
     * 过期时间
     */
    private long expires = 5000;

    /**
     * 获取锁的超时时间
     */
    private int timeout = 3000;

    /**
     * 是否获取到锁
     */
    private boolean locked = false;

    /**
     *
     * @param jedis     jedis 链接
     * @param lockKey   锁标识
     */
    public JedisLock(Jedis jedis, String lockKey) {
        this.jedis = jedis;
        this.lockKey = lockKey;
    }


    /**
     *
     * @param jedis     jedis 链接
     * @param lockKey   锁标识
     * @param expires   超时时间
     * @param timeout   请求阻塞过期时间
     */
    public JedisLock(Jedis jedis, String lockKey, long expires, int timeout) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.expires = expires;
        this.timeout = timeout;
    }

    /**
     * 得到锁
     * 非公平锁
     * 阻塞获取
     * @return
     * @throws InterruptedException
     */
    public synchronized boolean lock() throws InterruptedException {

        while (this.timeout >= 0) {
            String expiresStr = String.valueOf(System.currentTimeMillis() + this.expires);     //锁到期时间

            if (jedis.setnx(this.lockKey, expiresStr) == 1L) {
                this.locked = true;
                return true;
            }
            //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
            //获取上一个锁到期时间，并设置现在的锁到期时间，
            //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
            String currentValueStr = jedis.get(this.lockKey);
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                String oldValueStr = jedis.getSet(this.lockKey, expiresStr);
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    this.locked = true;
                    return true;
                }
            }

            this.timeout -= 100;
            Thread.sleep(100L);
        }

        return false;
    }

    /**
     * 释放锁
     */
    public synchronized void unlock() {
        if (this.locked) {
            this.jedis.del(this.lockKey);
            this.locked = false;
        }
        this.jedis.close();
    }


}
