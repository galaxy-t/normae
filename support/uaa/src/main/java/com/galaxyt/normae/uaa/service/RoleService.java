package com.galaxyt.normae.uaa.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxyt.normae.api.uaa.vo.RoleDetail;
import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.uaa.dao.RoleAuthorityDao;
import com.galaxyt.normae.uaa.dao.RoleDao;
import com.galaxyt.normae.uaa.dao.UserRoleDao;
import com.galaxyt.normae.uaa.pojo.dto.RoleAuthorityDto;
import com.galaxyt.normae.uaa.pojo.dto.RoleDto;
import com.galaxyt.normae.uaa.pojo.dto.RoleQueryDto;
import com.galaxyt.normae.uaa.pojo.po.Role;
import com.galaxyt.normae.uaa.pojo.po.RoleAuthority;
import com.galaxyt.normae.uaa.pojo.po.UserRole;
import com.galaxyt.normae.uaa.pojo.vo.RoleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色 业务层
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/6 15:01
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/6 15:01     jiangxd          v1.0.0           Created
 */
@Slf4j
@Service
@Component
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RoleAuthorityDao roleAuthorityDao;

    /**
     * 初始化系统管理员帐号
     */
    @Value("${system.administrator.username}")
    private String systemAdministratorUsername;


    /**
     * 角色新增
     * 新增角色及角色权限关系
     *
     * @param roleDto
     * @param currentUserId 当前用户id
     * @return
     */
    @Transactional
    public Long add(RoleDto roleDto, Long currentUserId) {

        LocalDateTime now = LocalDateTime.now();

        //创建role实例
        Role newRole = new Role();
        newRole.setApp(roleDto.getApp());
        newRole.setName(roleDto.getName());
        //唯一标识
        newRole.setMark(RandomUtil.randomString(4));
        newRole.setDescription(roleDto.getDescription());
        newRole.setDisabled(roleDto.getDisabled());
        newRole.setCreateUserId(currentUserId);
        newRole.setCreateTime(now);
        newRole.setUpdateUserId(currentUserId);
        newRole.setUpdateTime(now);

        //存证
        try {
            this.roleDao.insert(newRole);
        } catch (DuplicateKeyException e) { //唯一索引约束 , 角色已存在
            throw new GlobalException(GlobalExceptionCode.ROLE_NAME_EXISTS);
        }

        log.info("角色[{}]存储成功", newRole);

        return newRole.getId();
    }

    /**
     * 角色修改
     * 更新角色及角色权限关系
     *
     * @param currentUserId 当前用户id
     * @param roleId        角色id
     * @param roleDto       角色修改参数
     */
    @Transactional
    public void edit(Long currentUserId, Long roleId, RoleDto roleDto) {

        if (roleId == 0) {
            throw new GlobalException(GlobalExceptionCode.ADMIN_CANOT_EDIT);
        }
        //创建role实例
        Role newRole = new Role();
        newRole.setId(roleId);
        newRole.setName(roleDto.getName());
        newRole.setDescription(roleDto.getDescription());
        newRole.setDisabled(roleDto.getDisabled());
        newRole.setUpdateUserId(currentUserId);
        newRole.setUpdateTime(LocalDateTime.now());

        //存证
        int count = this.roleDao.updateById(newRole);
        if (count == 0) {
            throw new GlobalException(GlobalExceptionCode.ROLE_EDIT_FAILED);
        }
        log.info("角色[{}]更新成功", newRole);

    }

    /**
     * 角色删除
     * 删除角色及角色权限关系
     *
     * @param roleId
     * @return
     */
    @Transactional
    public void remove(Long roleId) {

        if (roleId == 0) {
            throw new GlobalException(GlobalExceptionCode.ADMIN_CANOT_EDIT);
        }
        //查看该角色是否绑定用户
        int count = this.userRoleDao.selectCount(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId));
        log.info("当前角色[{}]绑定用户数量为[{}]", roleId, count);
        if (count > 0) {
            throw new GlobalException(GlobalExceptionCode.ROLE_HAS_USER_FAILED);
        }

        //删除角色权限关系
        this.roleAuthorityDao.delete(new LambdaUpdateWrapper<RoleAuthority>().eq(RoleAuthority::getRoleId, roleId));
        log.info("当前角色[{}],权限关系已被删除", roleId);

        //删除角色
        int delCount = this.roleDao.deleteById(roleId);
        if (delCount == 0) {
            throw new GlobalException(GlobalExceptionCode.ROLE_DEL_FAILED);
        }
        log.info("当前角色[{}]已被删除", roleId);

    }

    /**
     * 获取角色列表
     *
     * @param page         分页
     * @param roleQueryDto 查询参数
     * @return
     */
    public void list(Page<RoleVo> page, RoleQueryDto roleQueryDto) {

        page = this.roleDao.selectRoleList(page, roleQueryDto);
        log.info("获取全部角色[{}]成功", page);

    }

    /**
     * 更新角色权限关系
     *
     * @param roleAuthorityDto
     */
    @Transactional
    public void authority(RoleAuthorityDto roleAuthorityDto) {

        //删除角色权限关系
        this.roleAuthorityDao.delete(new LambdaUpdateWrapper<RoleAuthority>().eq(RoleAuthority::getRoleId, roleAuthorityDto.getRoleId()));

        //更新角色权限关系
        List<RoleAuthority> roleAuthorityList = new ArrayList<>();
        for (Long l : roleAuthorityDto.getAuthorityIds()) {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRoleId(roleAuthorityDto.getRoleId());
            roleAuthority.setAuthorityId(l);
            roleAuthorityList.add(roleAuthority);
        }
        this.roleAuthorityDao.batchRoleAuthority(roleAuthorityList);
        log.info("角色[{}]菜单权限[{}]关系存储成功", roleAuthorityDto.getRoleId(), roleAuthorityDto.getAuthorityIds());

    }

    /**
     * 获取角色信息
     *
     * @param roleId
     */
    public RoleDetail detail(Long roleId) {

        //角色信息返回
        RoleDetail roleDetail = new RoleDetail();
        //roleId角色信息
        Role role = this.roleDao.selectById(roleId);
        BeanUtil.copyProperties(role, roleDetail);

        Optional<List<RoleAuthority>> roleAuthoritysOp = Optional.ofNullable(this.roleAuthorityDao.selectList(new LambdaQueryWrapper<RoleAuthority>()
                .eq(RoleAuthority::getRoleId, roleId)));
        roleAuthoritysOp.ifPresent(roleAuthoritys -> {
            List<Long> authorityIds = roleAuthoritys.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList());
            roleDetail.setAuthorityIds(authorityIds);
        });

        log.info("获取角色信息成功==>[{}]", roleDetail);
        return roleDetail;

    }

    /**
     * 角色列表
     * 用户选择角色列表
     */
    public Map<String, Object> list() {
        List<Role> roles = this.roleDao.selectList(new LambdaQueryWrapper<Role>().eq(Role::getApp, "iap2"));
        Map<String, Object> map = new HashMap<>();
        roles.forEach(role -> {
            map.put("roleId", role.getId());
            map.put("roleName", role.getName());
        });

        return map;
    }

}
