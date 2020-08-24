package com.galaxyt.normae.uaa.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.galaxyt.normae.core.enums.Disabled;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表
 * @author zhouqi
 * @date 2020/8/24 11:26
 * @version v1.0.0
 * @Description
 *
 */
@Data
@TableName("t_user")
public class User {

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

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

    /**
     * 是否启用
     */
    @TableField("disabled")
    private Integer disabled;

    /**
     * 是否删除
     */
    @TableField("deleted")
    private Integer deleted;

    /**
     * 创建者主键id
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改者主键id
     */
    @TableField("update_user_id")
    private Long updateUserId;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
