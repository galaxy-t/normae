package com.galaxyt.normae.uaa.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.galaxyt.normae.uaa.enums.Disabled;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限表
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/20 10:20
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/20 10:20     zhouqi          v1.0.0           Created
 */
@Data
@TableName("t_authority")
public class Authority {

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * app
     */
    @TableField("app")
    private String app;

    /**
     * 权限名称
     */
    @TableField("name")
    private String name;

    /**
     * 权限标识
     */
    @TableField("mark")
    private String mark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建者主键id
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 修改者主键id
     */
    @TableField("update_user_id")
    private Long updateUserId;

    /**
     * 是否启用
     */
    @TableField("disabled")
    private Disabled disabled;

}
