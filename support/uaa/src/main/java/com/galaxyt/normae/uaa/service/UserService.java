package com.galaxyt.normae.uaa.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.galaxyt.normae.api.uaa.dto.UaaUserDto;
import com.galaxyt.normae.api.uaa.vo.UserRoleVo;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.uaa.dao.AuthorityDao;
import com.galaxyt.normae.uaa.dao.RoleDao;
import com.galaxyt.normae.uaa.dao.UserDao;
import com.galaxyt.normae.uaa.dao.UserRoleDao;
import com.galaxyt.normae.uaa.enums.Disabled;
import com.galaxyt.normae.uaa.exception.UsernameNotFoundException;
import com.galaxyt.normae.uaa.exception.WrongPasswordException;
import com.galaxyt.normae.uaa.pojo.bo.SearchBo;
import com.galaxyt.normae.uaa.pojo.bo.UserAuthBo;
import com.galaxyt.normae.uaa.pojo.dto.UserRoleDto;
import com.galaxyt.normae.uaa.pojo.po.User;
import com.galaxyt.normae.uaa.pojo.po.UserRole;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户业务层
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/21 10:58
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/21 10:58     zhouqi          v1.0.0           Created
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AuthorityDao authorityDao;

    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 服务名称
     */
    @Value("${spring.application.name}")
    private String appName;

    /**
     * 系统超级管理员角色名称
     */
    @Value("${system.administrator.roleName}")
    private String systemAdministratorRoleName;

    /**
     * 系统超级管理员角色标识
     */
    @Value("${system.administrator.roleMark}")
    private String systemAdministratorRoleMark;

    /**
     * 初始化系统管理员帐号
     */
    @Value("${system.administrator.username}")
    private String systemAdministratorUsername;


    /**
     * 根据用户名和密码获取用户角色权限详情
     *
     * @param username 用户名
     * @param password 密码 , 不论加密未加密 , 若密码正确 , 该密码应该与数据库中的密码一致 , 密码可为空字符串 , 但不能为 null , 若为空字符串则判定为无密码登录 , 则不会再进行密码验证
     * @param app      所属项目
     */
    public UserAuthBo load(String username, String password, String app) throws UsernameNotFoundException, WrongPasswordException {

        /*
        根据用户名查询用户对象
        首先判断该用户是否为超级管理员
            若为超级管理员则不需要判断其所属 app
            否则需要对应到具体的 app
         */
        User user = null;
        if (username.equals(this.systemAdministratorUsername)) { //超级管理员
            user = this.userDao.selectOne(new QueryWrapper<User>()
                    .eq("username", username));
        } else {    //其它用户
            user = this.userDao.selectOne(new QueryWrapper<User>()
                    .eq("username", username)
                    .eq("app", app));
        }


        //将查询出来的用户对象包装成 Optional 对象
        Optional<User> userO = Optional.ofNullable(user);

        //若用户不存在则抛出异常
        userO.orElseThrow(UsernameNotFoundException::new);

        //先判断传入的密码是否为空字符串,若为空字符串则判定为无密码登录,不再进行密码验证
        //否则会进行密码验证
        if (!"".equals(password)) {
            //检查密码是否正确
            if (!user.getPassword().equals(password)) {
                throw new WrongPasswordException();
            }
        }

        List<SearchBo> roles = null;
        List<SearchBo> authoritys = null;

        if (username.equals(this.systemAdministratorUsername)) {    //超级管理员帐号默认的角色和全部的权限
            SearchBo roleBo = new SearchBo();
            roleBo.setId(0L);
            roleBo.setMark(this.systemAdministratorRoleMark);
            roles = Arrays.asList(roleBo);

            //查询该项目中的全部可用及未删除的权限
            authoritys = this.authorityDao.selectAllByApp(this.appName, Disabled.FALSE);
            authoritys.addAll(this.authorityDao.selectAllByApp(app, Disabled.FALSE));


        } else {    //普通用户从数据库中查询出其拥有的角色和权限
            roles = this.roleDao.selectByUserId(user.getId(), Disabled.FALSE);
            authoritys = this.authorityDao.selectByUserId(user.getId(), Disabled.FALSE);
        }

        //得到角色和权限全部的 ID
        List<Long> roleIds = roles.parallelStream().map(SearchBo::getId).distinct().collect(Collectors.toList());
        List<Long> authorityIds = authoritys.parallelStream().map(SearchBo::getId).distinct().collect(Collectors.toList());

        //得到角色和权限全部的标识
        List<String> roleMarks = roles.parallelStream().map(SearchBo::getMark).distinct().collect(Collectors.toList());
        List<String> authorityMarks = authoritys.parallelStream().map(SearchBo::getMark).distinct().collect(Collectors.toList());

        //封装成 UserAuthBo
        UserAuthBo userAuthBo = new UserAuthBo(user.getId(), username, roleMarks, authorityMarks, roleIds, authorityIds);

        return userAuthBo;
    }

    /**
     * 用户新增
     *
     * @param uaaUserDto
     */
    @Transactional
    public int add(UaaUserDto uaaUserDto) {

        User user = new User();
        BeanUtil.copyProperties(uaaUserDto, user);

        try {
            this.userDao.insert(user);
            return GlobalExceptionCode.SUCCESS.getCode();
        } catch (DuplicateKeyException e) { //唯一索引约束 , 用户名已存在
            return GlobalExceptionCode.USERNAME_ALREADY_EXISTS.getCode();
        }

    }

    /**
     * 修改密码
     *
     * @param uaaUserDto
     */
    @Transactional
    public boolean password(UaaUserDto uaaUserDto) {

        User user = new User();
        BeanUtil.copyProperties(uaaUserDto, user);

        //修改密码
        int count = this.userDao.updateById(user);
        if (count == 0) {
            return Boolean.FALSE;
        }

        log.info("用户[{}]修改密码成功", user.getId());
        return Boolean.TRUE;
    }


    /**
     * 用户删除
     * 根据用户id删除用户，以及用户权限关系
     *
     * @param userId 用户id
     */
    @Transactional
    public boolean remove(Long userId) {

        //删除用户角色关系
        this.userRoleDao.delete(new LambdaUpdateWrapper<UserRole>()
                .eq(UserRole::getUserId, userId));

        //删除用户
        int count = this.userDao.delete(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        if (count == 0) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * upinsert用户角色关系
     *
     * @param userRoleDto
     */
    @Transactional
    public void role(UserRoleDto userRoleDto) {

        List<UserRole> list = new ArrayList<>();
        for (Long roleId : userRoleDto.getRoleIds()) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userRoleDto.getUserId());
            list.add(userRole);
        }
        //删除mysql用户角色关系
        //删除用户角色关系
        this.userRoleDao.delete(new LambdaUpdateWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleDto.getUserId()));


        //存储用户角色关系到mysql
        this.userRoleDao.batchUserRole(list);
        log.info("用户[{}],角色[{}]关系存储成功", userRoleDto.getUserId(), userRoleDto.getRoleIds());
    }

    /**
     * 根据用户id查看用户角色id和角色名称
     *
     * @param userIds
     */
    public List<UserRoleVo> userRoleList(List<Long> userIds) {
        List<UserRoleVo> userRoleVos = this.userDao.userRoleList(Joiner.on(",").join(userIds));
        log.info("用户[{}],角色[{}]", userIds, userRoleVos);
        return userRoleVos;
    }
}
