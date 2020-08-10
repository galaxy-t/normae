package com.galaxyt.normae.core.thread;

import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 当前请求用户信息
 */
public class CurrentUser {


    /**
     * 当前线程是否进行过初始化
     */
    private static final ThreadLocal<AtomicBoolean> isInit = ThreadLocal.withInitial(AtomicBoolean::new);


    /**
     * 记录当前线程操作用户主键 ID
     */
    private static final ThreadLocal<Long> ID = new ThreadLocal<>();

    /**
     * 记录当前线程操作用户用户名
     */
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    /**
     * 初始化数据
     *
     * @param userId        当前登录用户主键 id
     * @param username      当前登录用户用户名
     */
    public static void init(long userId, String username) {

        /*
        若未进行初始化那么将对其进行值的初始化
         */
        if (isInit.get().compareAndSet(false, true)) {
            ID.set(userId);
            USERNAME.set(username);
        }

    }

    /**
     * 验证是否进行过初始化，若未进行初始化则抛出异常
     * 以防止某些不可预测的异常发生，导致获取到了空的主键 ID
     */
    private static void isInit() {

        if (isInit.get().compareAndSet(false, false)) {
            throw new GlobalException(GlobalExceptionCode.USER_LOGIN_STATUS_EXCEPTION);
        }

    }

    /**
     * 为防止线程池缓存这些变量，在每次请求结束之后需要主动清理他们
     */
    public static void destroy() {
        isInit.remove();
        ID.remove();
        USERNAME.remove();
    }


    /**
     * 获取当前登陆用户主键 ID
     * @return
     */
    public static Long getId() {
        isInit();
        return ID.get();
    }

    /**
     * 当前登录用户用户名
     * @return
     */
    public static String getUsername() {
        isInit();
        return USERNAME.get();
    }



}
