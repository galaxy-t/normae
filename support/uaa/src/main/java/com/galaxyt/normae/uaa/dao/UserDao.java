package com.galaxyt.normae.uaa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxyt.normae.api.uaa.vo.UserRoleVo;
import com.galaxyt.normae.uaa.pojo.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户 dao
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/20 10:27
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/20 10:27     zhouqi          v1.0.0           Created
 */
@Repository
public interface UserDao extends BaseMapper<User> {

    /**
     * 根据用户ids查看用户角色id和角色名称
     *
     * @param userIds
     */
    @Select(" SELECT r.id AS roleId, r.name AS roleName, ur.user_id AS userId FROM t_role r JOIN t_user_role ur ON r.id = ur.role_id WHERE ur.user_id IN (${userIds}) ")
    List<UserRoleVo> userRoleList(@Param("userIds") String userIds);

}
