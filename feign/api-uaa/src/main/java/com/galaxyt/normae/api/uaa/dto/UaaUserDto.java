package com.galaxyt.normae.api.uaa.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 注册或修改密码参数
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/28 14:52
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/28 14:52     zhouqi          v1.0.0           Created
 */
@Data
public class UaaUserDto {

    /**
     * 用户id
     * 用户新增和修改密码时必须传值
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 所属项目
     */
    private String app;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
