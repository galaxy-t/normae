package com.galaxyt.normae.uaa.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.galaxyt.normae.core.enums.Disabled;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限表
 * @author zhouqi
 * @date 2020/8/24 13:38
 * @version v1.0.0
 * @Description
 *
 */
@Data
@TableName("t_authority")
public class Authority {

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

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
     * 是否启用
     */
    @TableField("disabled")
    private Disabled disabled;

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
