package com.galaxyt.normae.uaa.service;

import com.galaxyt.normae.api.uaa.vo.UserDetail;
import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.util.seurity.JWTUtil;
import com.galaxyt.normae.uaa.exception.UsernameNotFoundException;
import com.galaxyt.normae.uaa.exception.WrongPasswordException;
import com.galaxyt.normae.uaa.pojo.bo.UserAuthBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 授权业务层
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/21 11:05
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/21 11:05     zhouqi          v1.0.0           Created
 */
@Service
public class AuthService {


    @Autowired
    private UserService userService;

    /**
     * JWT 的签名
     */
    @Value("${jwt.sign}")
    private String jwtSign;

    /**
     * JWT 的过期时间
     */
    @Value("${jwt.expiration}")
    private Long jwtExpiration;


    /**
     * 根据用户名获取 jwt 令牌
     *
     * @param username 用户名
     * @param password 密码 , 不论加密未加密 , 若密码正确 , 该密码应该与数据库中的密码一致 , 密码可为空字符串 , 但不能为 null , 若为空字符串则判定为无密码登录 , 则不会再进行密码验证
     * @param app      所属项目
     */
    public UserDetail auth(String username, String password, String app) {

        UserAuthBo userAuthBo = null;

        try {
            userAuthBo = this.userService.load(username, password, app);
        } catch (UsernameNotFoundException e) { //用户名不存在
            throw new GlobalException(GlobalExceptionCode.USERNAME_IS_NOT_FOUNT);
        } catch (WrongPasswordException e) {    //密码输入错误
            throw new GlobalException(GlobalExceptionCode.PASSWORD_IS_WRONG);
        }

        //生成 token
        String jwt = JWTUtil.INSTANCE.generate(String.valueOf(userAuthBo.getUserId()), userAuthBo.getUsername(), userAuthBo.getRoles(), userAuthBo.getAuthoritys(), username, this.jwtSign, this.jwtExpiration);
        //过期时间
        long expireTimestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() + jwtExpiration;
        LocalDateTime expireTime = LocalDateTime.ofEpochSecond(expireTimestamp / 1000, 0, ZoneOffset.ofHours(8));

        //返回体
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(userAuthBo.getUserId());
        userDetail.setUsername(userAuthBo.getUsername());
        userDetail.setToken(jwt);
        userDetail.setExpireTime(expireTime);
        userDetail.setRoleIds(userAuthBo.getRoleIds());
        userDetail.setAuthorityIds(userAuthBo.getAuthorityIds());
        return userDetail;

    }
}
