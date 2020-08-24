package com.galaxyt.normae.uaa.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录成功返回信息
 * @author zhouqi
 * @date 2020/8/24 13:21
 * @version v1.0.0
 * @Description
 *
 */
@Data
public class LoginVo {


    /**
     * 登录用户主键 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录成功后返回的 token
     */
    private String token;

    /**
     * token 到期时间
     */
    private LocalDateTime expireTime;

    /**
     * 用户所拥有的全部角色集合
     */
    private List<String> role;

    /**
     * 用户所拥有的全部权限集合
     */
    private List<String> authority;



}
