# jedis 封装

    本模块 jedis 做出了封装
    默认 JedisPool 的链接方式为单节点方式 , 具体可以在 com.galaxyt.normae.redis.JedisConfig 中做出修改 , 各种配置方式也已经写好
    封装原因 : Spring 提供的 redisTemplate 作者一直感觉不是很好用 , 尤其在中文处理方面还需要单独做出处理 , 用起来也不够直观
              其次 , SpringCache 或 JetCache 是基于注解来实现 redis 缓存的 , 首先类型转换方面就不是特别理想 , 再者因为 AOP 的原理
              在同一个类中一个方法调用另一个有注解的方法 , 该注解是无法生效的 , 但是在实际开发过程中刷新缓存这种事情却时常存在 , 所以便自己封装了一下
               
## 代码

    以下代码按照开发先后顺序排列

### RedisKey

    全局所需要使用到的 Key 都需要维护到该文件中作为常量存在 , 可以使用通配的方式

### JedisConfig

    用于配置 Jedis 与 Redis 链接的信息 , 如 host,port,password,超时时间等
    默认使用 JedisPool
    若 Redis 安装方式为 哨兵或Cluster 模式则需要开放下面的注释

### JedisClient

    在最早使用 Jedis 的时候 , 我一般要求开发人员直接在代码中 , 如 UserService 中直接   private JedisPool jedisPool; 注入 JedisPool 对象 , 
    然后使用的时候直接 this.jedisPool.getResource() 得到一个 Jedis 实例 , 但是 Jedis 实例使用完之后需要手动的调用 jedis.close() 将其还给 JedisPool , 
    但是开发人员往往经常忘掉 , 而且该端代码的书写效果也不是很好 , 所以自行封装了一个 JedisClient 客户端 , 使用的时候直接 private JedisClient jedisClient; 注入 .
    
    使用: 
    operate()   操作方法 , 使用该方法来进行 redis 的操作 , 该方法会在结果返回之前调用 jedis.close();
    例1: 
    String value = this.jedisClient.operate(jedis -> {
        return jedis.get("key");
    });
    例2: 
    String value = jedisClient.operate(jedis -> jedis.get("key"));
    
    getJedis()  该方法主要方便开发人员需要一个 jedis 实例的时候来获取该实例 , 但是需要注意的是开发人员需要自行调用 jedis.close();
    
### JedisLock

    基于 redis 的分布式锁
    非公平锁
    默认在竞争锁的时候超过 3000 毫秒则会放弃获取锁
    默认最大持有时间为 5000 毫秒
    
    使用:
    构造函数: 创建一个 JedisLock 对象 , 需要自行传入 jedis 实例(this.jedisClient.getJedis())和该锁的 key(应注意锁的范围越小越好 , 不应该大面积锁住多个线程)
    JedisLock(Jedis jedis, String lockKey)      
    JedisLock(Jedis jedis, String lockKey, long expires, int timeout)   手动设置超时时间和过期时间
     
    lock()  获取锁 , 默认最大等待 3000 毫秒
    unlock() 释放锁 , 开发人员应该确保该方法最终一定会被执行 , 即使获取不到锁 , 也要执行一下该方法 , 该方法内部会执行 this.jedis.close();
             若不执行该方法 , 会导致 Jedis 实例无法归还到 JedisPool , 会导致后面无法再次获取到 Jedis 实例

### JedisCache

    首先吐槽一点 , 在很多情况下 , 基于注解的 AOP 用起来确实不怎么好用 , 如 SpringCache,JetCache 这种缓存框架 , 
    搭配 Redis 来用的时候 , 问题和不方便的地方还是很多的 , 类型转换,同一个类中的两个有注解的方法调用 , 被调用者的注解无法生效等 
    
    自己写了一个 , 其实就是操作了一下 Redis , 把通用的地方封装起来了 , 虽然存在代码侵入性 , 但是相对来说用起来还是挺顺手的
    
    类型转换基于 Gson 实现 , 别的不知道 , 反正我就觉得 Gson 用起来确实挺顺手的 , 在此安利一下 google
    
    使用: 
    public <R> R able(final String key, Class c, Supplier<R> supplier)
    public <R> R able(final String key, Class c, final int timeout, Supplier<R> supplier)
    以上两个方法都是用来添加缓存的 , 即 参数中的 key 在 redis 中存在则使用 redis 中的 value 作为结果 , 否则执行 supplier
    第二个参数用于指定返回值的转换类型
    第二个方法的timeout参数是用来指定该缓存多长时间失效
    例: 
    User user = this.jedisCache.able("user", User.class, () -> {
        User user = jdbc.findById(1);
        return user;
    });
    以上方法的 Supplier 函数中其实本质上愿意咋写就咋写 , 只不过需要返回一个需要的类型实例即可 , 
    注意要缓存的内容 , 不能够出现枚举 , 尽可能的使用基础类型 , 因为本质上是 gson 在做序列化和反序列化 , 如果 gson 转不成功那就不要进行尝试了
    
    public <R> R put(final String key, Supplier<R> supplier)       
    public <R> R put(final String key, final int timeout, Supplier<R> supplier)    
    以上两个方法的效果与 able 相同 , 只不过每次都会执行 supplier
    第二个方法的timeout参数是用来指定该缓存多长时间失效        
    
    public void evict(final String key)
    提供一个 key 来删除一个缓存      
    
## 彩蛋

    以下附带一个 redis 单机操作脚本和一个配置文件

### 配置文件 [redis.conf](redis.conf)

### 脚本 [redis.sh](redis.sh)

