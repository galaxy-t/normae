package com.galaxyt.normae.uaa.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.util.seurity.JWTUtil;
import com.galaxyt.normae.uaa.dao.AuthorityDao;
import com.galaxyt.normae.uaa.dao.RoleDao;
import com.galaxyt.normae.uaa.dao.UserDao;
import com.galaxyt.normae.core.enums.Disabled;
import com.galaxyt.normae.uaa.pojo.po.User;
import com.galaxyt.normae.uaa.pojo.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 登录
 * @author zhouqi
 * @date 2020/8/24 11:32
 * @version v1.0.0
 * @Description
 *
 */
@Service
public class LoginService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AuthorityDao authorityDao;


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
     * 登录方法
     *
     * @param username  用户名
     * @param password  密码 , 本方法不考虑加密问题 , 若密码需要加密应由调用者进行加密传入 , 密码可以为 null , 若为 null 则判定为无密码登录(如短信验证码登录) , 此时调用方应自行解决短信验证码验证问题
     *
     */
    public LoginVo login(String username, String password) {

        //根据用户名查询用户对象1
        User user = this.userDao.selectOne(new QueryWrapper<User>()
                .eq("username", username));

        //若用户不存在则抛出异常
        if (user == null) {
            throw new GlobalException(GlobalExceptionCode.USERNAME_IS_NOT_FOUNT);
        }

        //若初入密码则判断密码是否正确 , 若密码错误则抛出异常
        if (password != null) {
            if (!password.equals(user.getPassword())) {
                throw new GlobalException(GlobalExceptionCode.PASSWORD_IS_WRONG);
            }
        }

        //查询用户所拥有的角色和权限的标识集合
        List<String> roleArr = this.roleDao.selectByUserId(user.getId(), Disabled.FALSE);
        List<String> authorityArr = this.authorityDao.selectByUserId(user.getId(), Disabled.FALSE);

        //生成 token
        String jwt = JWTUtil.INSTANCE.generate(String.valueOf(user.getId()), user.getUsername(), roleArr, authorityArr, username, this.jwtSign, this.jwtExpiration);

        //过期时间
        long expireTimestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() + jwtExpiration;
        LocalDateTime expireTime = LocalDateTime.ofEpochSecond(expireTimestamp / 1000, 0, ZoneOffset.ofHours(8));

        //封装返回值并返回
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(user.getId());
        loginVo.setUsername(user.getUsername());
        loginVo.setToken(jwt);
        loginVo.setExpireTime(expireTime);
        loginVo.setRole(roleArr);
        loginVo.setAuthority(authorityArr);

        return loginVo;
    }





}
