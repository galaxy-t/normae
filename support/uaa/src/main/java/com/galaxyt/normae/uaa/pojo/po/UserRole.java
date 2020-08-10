package com.galaxyt.normae.uaa.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户角色关联表
 * @author zhouqi
 * @date 2020/5/20 16:23
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/20 16:23     zhouqi          v1.0.0           Created
 *
 */
@Data
@TableName("t_user_role")
public class UserRole {

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 用户 id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色 id
     */
    @TableField("role_id")
    private Long roleId;

}
