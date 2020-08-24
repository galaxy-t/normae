package com.galaxyt.normae.core.enums;


/**
 * 删除状态
 * 仅作为删除状态 0 和 1 取值使用 , 不可被作为属性类型能使用
 * 代码中如果需要传递或者判断删除状态必须使用该枚举
 * 如: Deleted.TRUE.getCode()    只能这样写 , 不可以直接写 1
 * @author zhouqi
 * @date 2020/8/24 13:45
 * @version v1.0.0
 * @Description
 *
 */
public enum Deleted {

    TRUE(1, "已删除"),
    FALSE(0, "未删除"),

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
    Deleted(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return msg;
    }


}
