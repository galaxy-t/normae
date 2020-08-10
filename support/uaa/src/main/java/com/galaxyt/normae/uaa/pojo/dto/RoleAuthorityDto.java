package com.galaxyt.normae.uaa.pojo.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色权限dto
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/15 10:29
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/15 10:29     jiangxd          v1.0.0           Created
 */
@Data
@ApiModel(description = "角色权限")
public class RoleAuthorityDto {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /**
     * 权限id
     */
    @ApiModelProperty("权限id")
    @NotEmpty(message = "权限id不能为空")
    private List<Long> authorityIds;

}
