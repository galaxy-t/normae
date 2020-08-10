package com.galaxyt.normae.uaa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxyt.normae.core.enums.Deleted;
import com.galaxyt.normae.uaa.enums.Disabled;
import com.galaxyt.normae.uaa.pojo.bo.SearchBo;
import com.galaxyt.normae.uaa.pojo.po.Authority;
import com.galaxyt.normae.uaa.pojo.vo.AuthorityVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限 dao
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
public interface AuthorityDao extends BaseMapper<Authority> {

    /**
     * 查询全部的权限列表
     * 未删除且未禁用
     *
     * @return
     */
    @Select(" SELECT a.id,a.mark FROM t_authority a WHERE a.app = #{app} AND a.disabled = #{disabled.code} ")
    List<SearchBo> selectAllByApp(@Param("app") String app, @Param("disabled") Disabled disabled);

    /**
     * 查询全部的权限列表
     * 未删除
     *
     * @return
     */
    Page<AuthorityVo> selectAuthorityList(Page<AuthorityVo> page, @Param("app") String app);

    /**
     * 根据用户 id 查询该用户全部的权限
     *
     * @param userId
     * @param disabled
     * @return
     */
    @Select(" SELECT a.id,a.mark FROM t_user_role ur LEFT JOIN t_role_authority ra ON ur.role_id = ra.role_id LEFT JOIN t_authority a ON ra.authority_id = a.id WHERE ur.user_id = #{userId} AND a.disabled = #{disabled.code} ")
    List<SearchBo> selectByUserId(@Param("userId") Long userId, @Param("disabled") Disabled disabled);

}
