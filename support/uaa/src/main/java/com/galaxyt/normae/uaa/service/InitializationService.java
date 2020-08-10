package com.galaxyt.normae.uaa.service;

import com.galaxyt.normae.uaa.dao.RoleDao;
import com.galaxyt.normae.uaa.dao.UserDao;
import com.galaxyt.normae.uaa.dao.UserRoleDao;
import com.galaxyt.normae.uaa.enums.Disabled;
import com.galaxyt.normae.uaa.pojo.po.Role;
import com.galaxyt.normae.uaa.pojo.po.User;
import com.galaxyt.normae.uaa.pojo.po.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * 初始化代码
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/20 16:01
 * @Description 若检测到系统数据库中不存在 admin 用户则创建一个系统管理员帐号
 * 并为其创建 administrator 角色 , 该角色默认会带有全部的权限
 * 程序会以数据库中是否存在该用户和 administrator 作为是否进行过初始化的判断依据 , 若进行过初始化则不会再进行初始化操作
 * <p>
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/20 16:01     zhouqi          v1.0.0           Created
 */
@Slf4j
@Component
public class InitializationService implements CommandLineRunner {


    /**
     * 初始化系统管理员帐号
     */
    @Value("${system.administrator.username}")
    private String systemAdministratorUsername;

    /**
     * 初始化系统管理员密码
     */
    @Value("${system.administrator.password}")
    private String systemAdministratorPassword;

    /**
     * 超级管理员名称
     */
    private static final String ROLE_ADMINISTRATOR_NAME = "超级管理员";

    /**
     * 超级管理员角色
     */
    private static final String ROLE_ADMINISTRATOR_MARK = "ROLE_ADMINISTRATOR";


    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        log.info("UAA 服务系统初始化 start");

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(0L);
        user.setUsername(this.systemAdministratorUsername);
        user.setPassword(this.systemAdministratorPassword);

        Role role = new Role();
        role.setId(0L);
        role.setName(ROLE_ADMINISTRATOR_NAME);
        role.setMark(ROLE_ADMINISTRATOR_MARK);
        role.setCreateTime(now);
        role.setUpdateTime(now);
        role.setCreateUserId(0L);
        role.setUpdateUserId(0L);
        role.setDisabled(Disabled.FALSE);

        try {
            this.userDao.insert(user);
            this.roleDao.insert(role);

            UserRole userRole = new UserRole();
            userRole.setUserId(0L);
            userRole.setRoleId(0L);
            this.userRoleDao.insert(userRole);
        } catch (Exception e) {

        }
        log.info("UAA 服务系统初始化 end");
    }

}
