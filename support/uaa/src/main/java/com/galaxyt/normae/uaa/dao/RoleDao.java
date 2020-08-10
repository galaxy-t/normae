package com.galaxyt.normae.uaa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxyt.normae.core.enums.Deleted;
import com.galaxyt.normae.uaa.enums.Disabled;
import com.galaxyt.normae.uaa.pojo.bo.SearchBo;
import com.galaxyt.normae.uaa.pojo.dto.RoleQueryDto;
import com.galaxyt.normae.uaa.pojo.po.Role;
import com.galaxyt.normae.uaa.pojo.vo.RoleVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色 dao
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/20 10:28
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/20 10:28     zhouqi          v1.0.0           Created
 */
@Repository
public interface RoleDao extends BaseMapper<Role> {

    /**
     * 根据用户 id 查询该用户全部的角色
     *
     * @param userId
     * @param disabled
     * @return
     */
    @Select(" SELECT r.id,r.mark FROM t_user_role ur INNER JOIN t_role r ON ur.role_id = r.id WHERE ur.user_id = #{userId} AND r.disabled = #{disabled.code} ")
    List<SearchBo> selectByUserId(@Param("userId") Long userId, @Param("disabled") Disabled disabled);

    /**
     * 角色列表
     * 条件查询
     * 分页查询
     *
     * @param page
     * @param queryDto
     */
    Page<RoleVo> selectRoleList(Page<RoleVo> page, @Param("queryDto") RoleQueryDto queryDto);

}
