package com.galaxyt.normae.core.util;

import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.google.common.io.CharStreams;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * HTTP 工具类
 * 将 connection 链接做成最简化版本，并且使其拥有 http 池
 * 其中的请求方法只有成功和抛出异常两种情况，调用者必须进行异常捕获，并对请求失败的情况作出相应的处理
 * 本类中不进行任何的代码提取，开发者认为针对本项目的 http 请求操作，只有成功和失败，而且不存在其它的情况，失败就进行重试等操作
 * 所以最简洁的代码反而会起到更好的效果
 */
public enum HttpUtil {

    INSTANCE;

    /**
     * 默认连接超时时间
     */
    private static int CONNECT_TIMEOUT = 1000; //1 秒

    /**
     * 响应结果读取超时时间
     */
    private static final int READ_TIMEOUT = 3000;


    /**
     * get 请求
     *
     * @param url
     * @param serializeFunction
     * @param <R>
     * @return
     */
    public <R> R get(String url, Function<String, R> serializeFunction) {

        HttpURLConnection connection = null;

        InputStreamReader isr = null;
        InputStreamReader esr = null;

        int responseCode = 0;           //响应状态码
        String responseBody = null;     //响应内容字符串

        try {
            URL urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();

            //设置超时时间
            //下面两个值限定了如果打开链接超过 CONNECT_TIMEOUT 毫秒 或者 读取响应结果超过 READ_TIMEOUT 毫秒都会抛出异常
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestMethod("GET");

            connection.connect();   //打开链接

            //得到响应状态吗
            responseCode = connection.getResponseCode();
            //读取响应结果
            isr = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            responseBody = CharStreams.toString(isr);

        } catch (Exception e) {
            e.printStackTrace();

            if (connection != null) {
                if (connection.getErrorStream() != null) {
                    // 参考：https://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html
                    try {
                        esr = new InputStreamReader(connection.getErrorStream());
                        CharStreams.toString(esr);
                    } catch (Exception e1) {    //若读取异常信息都会出现问题则忽略这个异常
                        e1.printStackTrace();
                    }
                }
            }

        } finally {
            try {
                if (isr != null) {  //关闭输入流
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (esr != null) {  //关闭异常输入流
                    esr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //到此，若响应状态吗为 200 则表示本次请求全部正常结束
        if (responseCode == 200) {
            return serializeFunction.apply(responseBody);
        }

        //其它情况均判定为请求失败
        throw new GlobalException(GlobalExceptionCode.HTTP_REQUEST_ERROR, String.format("请求[%s]失败", url));
    }

    /**
     * post 请求
     *
     * @param url
     * @param body
     * @param serializeFunction
     * @param <R>
     * @return
     */
    public <R> R post(String url, String body, Function<String, R> serializeFunction) {

        HttpURLConnection connection = null;

        InputStreamReader isr = null;
        InputStreamReader esr = null;

        int responseCode = 0;           //响应状态码
        String responseBody = null;     //响应内容字符串

        try {
            URL urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();

            //设置超时时间
            //下面两个值限定了如果打开链接超过 CONNECT_TIMEOUT 毫秒 或者 读取响应结果超过 READ_TIMEOUT 毫秒都会抛出异常
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestMethod("POST");
            //设置是否向HttpURLConnection输出
            connection.setDoOutput(true);
            //设置是否从httpUrlConnection读入
            connection.setDoInput(true);
            //设置是否使用缓存
            connection.setUseCaches(false);
            //设置参数类型是json格式
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.connect();   //打开链接

            //放入body
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(body);
            writer.close();

            //得到响应状态吗
            responseCode = connection.getResponseCode();
            //读取响应结果
            isr = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            responseBody = CharStreams.toString(isr);

        } catch (Exception e) {
            e.printStackTrace();

            if (connection != null) {
                if (connection.getErrorStream() != null) {
                    // 参考：https://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html
                    try {
                        esr = new InputStreamReader(connection.getErrorStream());
                        CharStreams.toString(esr);
                    } catch (Exception e1) {    //若读取异常信息都会出现问题则忽略这个异常
                        e1.printStackTrace();
                    }
                }
            }

        } finally {
            try {
                if (isr != null) {  //关闭输入流
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (esr != null) {  //关闭异常输入流
                    esr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //到此，若响应状态吗为 200 则表示本次请求全部正常结束
        if (responseCode == 200) {
            return serializeFunction.apply(responseBody);
        }

        //其它情况均判定为请求失败
        throw new GlobalException(GlobalExceptionCode.HTTP_REQUEST_ERROR, String.format("请求[%s]失败", url));
    }

}
