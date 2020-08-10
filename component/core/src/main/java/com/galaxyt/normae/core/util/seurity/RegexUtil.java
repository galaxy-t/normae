package com.galaxyt.normae.core.util.seurity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * @author zhouqi
 * @date 2020/5/28 16:22
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/28 16:22     zhouqi          v1.0.0           Created
 *
 */
public class RegexUtil {

    /**
     * 手机号
     */
    public static final String REGEX_PHONE_NUMBER = "^1[3|4|5|7|8][0-9]\\d{4,8}$";


    /**
     * 验证手机号是否正确
     * @param phoneNumber
     * @return
     */
    public static boolean phoneNumber(String phoneNumber) {

        //若传入的电话号码为 null 或 不为 11 位则返回 false
        if (phoneNumber == null || phoneNumber.length() != 11) {
            return Boolean.FALSE;
        }

        Pattern pattern = Pattern.compile(REGEX_PHONE_NUMBER);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


}
