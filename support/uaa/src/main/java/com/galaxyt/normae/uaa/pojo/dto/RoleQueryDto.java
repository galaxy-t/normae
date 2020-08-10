package com.galaxyt.normae.uaa.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 条件查询uaa role列表
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/10 12:36
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/10 12:36     jiangxd          v1.0.0           Created
 */
@Data
@ApiModel(description = "角色查询参数")
public class RoleQueryDto {

    /**
     * 所属项目
     */
    @ApiModelProperty("所属项目")
    @NotBlank(message = "所属项目不能为空")
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

}
