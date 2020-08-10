package com.galaxyt.normae.uaa.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色权限关系表
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/20 10:19
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/20 10:19     zhouqi          v1.0.0           Created
 */
@Data
@TableName("t_role_authority")
public class RoleAuthority {

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 角色 id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 权限 id
     */
    @TableField("authority_id")
    private Long authorityId;

}
