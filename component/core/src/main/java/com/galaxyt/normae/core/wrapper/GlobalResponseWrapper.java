package com.galaxyt.normae.core.wrapper;


import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.util.json.GsonUtil;
import lombok.Data;

/**
 * 返回值装载器
 *
 * @author zhouqi
 * @date 2020/5/15 17:27
 * @version v1.0.0
 * @Description
 *  全部的API接口的返回信息必须返回成本类型的对象进行返回
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/15 17:27     zhouqi          v1.0.0           Created
 *
 */
@Data
public class GlobalResponseWrapper {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 要返回的数据
     */
    private Object data;

    /**
     * 创建一个 GlobalResponseWrapper 实例
     * 并使用默认的成功状态
     */
    public GlobalResponseWrapper() {
        this(GlobalExceptionCode.SUCCESS);
    }

    /**
     * 创建一个 GlobalResponseWrapper 实例
     * 根据指定的状态
     * @param code
     */
    public GlobalResponseWrapper(GlobalExceptionCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    /**
     * 设置状态信息
     * @param msg
     * @return
     */
    public GlobalResponseWrapper msg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 设置返回值内容
     * @param data
     * @return
     */
    public GlobalResponseWrapper data(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return GsonUtil.getJson(this);
    }
}
