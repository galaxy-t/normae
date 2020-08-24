package com.galaxyt.normae.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;

/**
 * 禁用状态
 * @author zhouqi
 * @date 2020/8/24 13:34
 * @version v1.0.0
 * @Description
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Disabled {

    TRUE(1, "已禁用"),
    FALSE(0, "未禁用"),

    ;

    /**
     * 代码
     */
    private final int code;

    /**
     * 内容
     */
    private final String msg;


    /**
     * @param code 代码
     * @param msg  内容
     */
    Disabled(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据代码获取枚举
     * 不要尝试缓存全部的枚举，该方法用到的频率不会太高，且枚举很少，不会造成资源浪费
     * <p>
     * 使用 @JsonCreator 让 jackson 解析 json 的时候能匹配到该枚举
     * 参考：https://segmentfault.com/q/1010000020636087
     *
     * @param code 枚举code
     * @return CertificationStatusEnum
     */
    @JsonCreator
    public static Disabled getByCode(int code) {

        Disabled[] values = Disabled.values();

        for (Disabled value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new GlobalException(GlobalExceptionCode.NOT_FOUND_ENUM, String.format("未找到 [%s] 对应的禁用状态!", code));
    }
}
