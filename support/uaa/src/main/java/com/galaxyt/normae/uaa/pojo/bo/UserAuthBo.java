package com.galaxyt.normae.uaa.pojo.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户角色权限封装对象
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/21 11:07
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/21 11:07     zhouqi          v1.0.0           Created
 */
@Data
public class UserAuthBo {

    public UserAuthBo(Long userId, String username, List<String> roles, List<String> authoritys, List<Long> roleIds, List<Long> authorityIds) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.authoritys = authoritys;
        this.roleIds = roleIds;
        this.authorityIds = authorityIds;
    }

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户所拥有的角色 mark
     */
    private List<String> roles;

    /**
     * 用户所拥有的权限 mark
     */
    private List<String> authoritys;

    /**
     * 用户所拥有的角色 id
     */
    private List<Long> roleIds;

    /**
     * 用户所拥有的权限 id
     */
    private List<Long> authorityIds;

}
