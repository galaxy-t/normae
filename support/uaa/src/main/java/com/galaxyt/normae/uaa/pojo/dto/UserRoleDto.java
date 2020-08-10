package com.galaxyt.normae.uaa.pojo.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 存储用户角色关系
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/6 14:46
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/6 14:46     jiangxd          v1.0.0           Created
 */
@Data
public class UserRoleDto {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 角色id
     */
    @NotEmpty(message = "角色id不能为空")
    private List<Long> roleIds;

}



