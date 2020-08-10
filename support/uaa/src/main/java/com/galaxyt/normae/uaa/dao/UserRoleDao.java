package com.galaxyt.normae.uaa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxyt.normae.uaa.pojo.po.UserRole;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联表 dao
 * @author zhouqi
 * @date 2020/5/20 16:26
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/20 16:26     zhouqi          v1.0.0           Created
 *
 */
@Repository
public interface UserRoleDao extends BaseMapper<UserRole> {


    /**
     * @return int
     * @Author jxd
     * @Description 批量新增用户角色信息
     * @Date 15:12 2020/6/30
     * @Param [userRoleList]
     **/
    int batchUserRole(List<UserRole> userRoleList);

}
