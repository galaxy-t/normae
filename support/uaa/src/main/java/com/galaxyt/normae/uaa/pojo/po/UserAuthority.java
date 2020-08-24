package com.galaxyt.normae.uaa.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户权限表
 * 为用户添加除角色以外的其它权限
 * @author zhouqi
 * @date 2020/8/24 11:50
 * @version v1.0.0
 * @Description
 *
 */
@Data
@TableName("t_user_authority")
public class UserAuthority {

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 用户 id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 权限 id
     */
    @TableField("authority_id")
    private Long authorityId;

}
