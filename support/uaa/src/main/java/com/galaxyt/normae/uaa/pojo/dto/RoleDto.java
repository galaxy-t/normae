package com.galaxyt.normae.uaa.pojo.dto;

import com.galaxyt.normae.uaa.enums.Disabled;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色新增
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/9 13:47
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/9 13:47     jiangxd          v1.0.0           Created
 */
@Data
@ApiModel(description = "角色新增参数")
public class RoleDto {

    /**
     * 所属项目
     */
    @ApiModelProperty("所属项目")
    private String app;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String name;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    private String description;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Disabled disabled;

}
