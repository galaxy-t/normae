package com.galaxyt.normae.uaa.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.galaxyt.normae.uaa.enums.Disabled;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/20 10:15
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/20 10:15     zhouqi          v1.0.0           Created
 */
@Data
@TableName("t_user")
public class User {

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 所属项目
     */
    @TableField("app")
    private String app;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

}
