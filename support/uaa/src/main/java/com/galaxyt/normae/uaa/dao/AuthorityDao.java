package com.galaxyt.normae.uaa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxyt.normae.core.enums.Disabled;
import com.galaxyt.normae.uaa.pojo.po.Authority;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限 dao
 * @author zhouqi
 * @date 2020/8/24 13:38
 * @version v1.0.0
 * @Description
 *
 */
@Repository
public interface AuthorityDao extends BaseMapper<Authority> {


    @Select(" SELECT a.mark FROM t_user_role ur LEFT JOIN t_role_authority ra ON ur.role_id = ra.role_id LEFT JOIN t_authority a ON ra.authority_id = a.id WHERE ur.user_id = #{userId} AND a.disabled = #{disabled.code} UNION SELECT a.mark FROM t_user_authority ua LEFT JOIN t_authority a ON ua.authority_id = a.id WHERE ua.user_id = #{userId} AND a.disabled = #{disabled.code} ")
    List<String> selectByUserId(@Param("userId") Long userId, @Param("disabled") Disabled disabled);


}
