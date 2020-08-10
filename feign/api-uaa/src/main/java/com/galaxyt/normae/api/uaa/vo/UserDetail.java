package com.galaxyt.normae.api.uaa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户详情
 * 授权返回
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
public class UserDetail {

    /**
     * 用户 id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    /**
     * 用户所拥有的角色
     */
    private List<Long> roleIds;

    /**
     * 用户所拥有的权限
     */
    private List<Long> authorityIds;

}
