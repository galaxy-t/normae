package com.galaxyt.normae.core.util.math;

import java.util.Random;

/**
 * 数字工具类
 */
public class NumberUtil {



    /**
     * 生成一个指定长度的随机数
     * 若第一个数字是 0 的情况下返回 int 会使其丢失应有的长度 , 所以此处的返回值为 String
     * @param length
     * @return
     */
    public static String random(int length) {

        /*
        实例化一个随机数对象
        虽然 Random 实例是线程安全的 , 但会因为竞争同一个 seed 导致性能下降
        所以此处每次都实例化一个 Random
         */
        Random ramdom = new Random();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++) {
            sb.append(ramdom.nextInt(10));
        }

        return sb.toString();
    }


}
