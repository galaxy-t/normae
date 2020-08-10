package com.galaxyt.normae.api.uaa.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 用户角色
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/15 14:14
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/15 14:14     jiangxd          v1.0.0           Created
 */
@Data
public class UserRoleVo {

    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 角色id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

}
