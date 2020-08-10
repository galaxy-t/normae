package com.galaxyt.normae.uaa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.uaa.dao.AuthorityDao;
import com.galaxyt.normae.uaa.dao.RoleAuthorityDao;
import com.galaxyt.normae.uaa.enums.Disabled;
import com.galaxyt.normae.uaa.pojo.dto.AuthorityDto;
import com.galaxyt.normae.uaa.pojo.po.Authority;
import com.galaxyt.normae.uaa.pojo.po.RoleAuthority;
import com.galaxyt.normae.uaa.pojo.vo.AuthorityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 权限 Service
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/13 15:33
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/13 15:33     jiangxd          v1.0.0           Created
 */
@Slf4j
@Service
public class AuthorityService {

    @Autowired
    private AuthorityDao authorityDao;

    @Autowired
    private RoleAuthorityDao roleAuthorityDao;


    /**
     * 权限新增
     *
     * @param authorityDto
     * @return
     */
    public void add(AuthorityDto authorityDto) {

        //创建authority对象并赋值
        Authority newAuthority = new Authority();
        newAuthority.setApp(authorityDto.getApp());
        newAuthority.setName(authorityDto.getName());
        newAuthority.setMark(authorityDto.getMark());
        newAuthority.setCreateUserId(authorityDto.getOperator());
        newAuthority.setUpdateUserId(authorityDto.getOperator());
        newAuthority.setCreateTime(LocalDateTime.now());
        newAuthority.setUpdateTime(LocalDateTime.now());
        newAuthority.setDisabled(Disabled.FALSE);

        try {
            this.authorityDao.insert(newAuthority);
        } catch (Exception e) { //唯一索引约束 , 权限名已存在
            throw new GlobalException(GlobalExceptionCode.AUTHORITY_ALREADY_EXISTS);
        }
        log.info("权限[{}]新增成功", newAuthority);

    }

    /**
     * 权限修改
     *
     * @param authorityId
     * @param authorityDto
     * @return
     */
    public void edit(Long authorityId, AuthorityDto authorityDto) {

        //创建authority对象并赋值
        Authority newAuthority = new Authority();
        newAuthority.setId(authorityId);
        newAuthority.setName(authorityDto.getName());
        newAuthority.setMark(authorityDto.getMark());
        newAuthority.setUpdateUserId(authorityDto.getOperator());
        newAuthority.setUpdateTime(LocalDateTime.now());

        //权限修改
        int count = this.authorityDao.updateById(newAuthority);
        if (count == 0) {
            //权限删除失败
            throw new GlobalException(GlobalExceptionCode.AUTHORITY_DEL_FAILED);
        }
        log.info("权限[{}]修改成功", newAuthority);

    }

    /**
     * 权限列表
     *
     * @return
     */
    public void list(Page<AuthorityVo> page, String app) {

        page = this.authorityDao.selectAuthorityList(page, app);
        log.info("权限[{}]获取成功", page);

    }

    /**
     * 权限删除
     *
     * @param authorityId
     */
    public void remove(Long authorityId) {

        //判断角色是否分配有该权限，若分配则无法直接删除
        int count = this.roleAuthorityDao.selectCount(new LambdaQueryWrapper<RoleAuthority>()
                .eq(RoleAuthority::getAuthorityId, authorityId));
        if (count > 0) {
            throw new GlobalException(GlobalExceptionCode.AUTHORITY_ALREADY_USERD);
        }

        //删除权限
        int delNum = this.authorityDao.deleteById(authorityId);
        if (delNum == 0) {
            throw new GlobalException(GlobalExceptionCode.AUTHORITY_DEL_FAILED);
        }
        log.info("权限[{}]删除成功", authorityId);
    }

    /**
     * 权限启用禁用
     *
     * @param authorityId
     */
    public void disabled(Long authorityId) {

        Authority authority = this.authorityDao.selectById(authorityId);

        if (authority == null) {
            throw new GlobalException(GlobalExceptionCode.AUTHORITY_NOT_FOUND);
        }

        //设置禁用状态
        authority.setDisabled(authority.getDisabled() == Disabled.TRUE ? Disabled.FALSE : Disabled.TRUE);

        //状态更新
        int count = this.authorityDao.updateById(authority);
        if (count == 0) {
            throw new GlobalException(GlobalExceptionCode.AUTHORITY_STATUS_FAILED);
        }

    }

}
