package com.galaxyt.normae.uaa.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 权限新增修改所需参数
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/7/13 15:14
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/13 15:14     zhouqi          v1.0.0           Created
 */
@Data
@ApiModel(description = "权限新增修改所需参数")
public class AuthorityDto {

    /**
     * 所属项目
     */
    @NotBlank(message = "所属项目不能为空")
    @ApiModelProperty("所属项目")
    private String app;

    /**
     * 权限名称
     * 有开发人员自行定义
     */
    @NotBlank(message = "权限名称不能为空")
    @ApiModelProperty("权限名称")
    private String name;

    /**
     * 权限标识
     * 同一个项目中(模块)应唯一
     */
    @NotBlank(message = "权限标识不能为空")
    @ApiModelProperty("权限标识")
    private String mark;

    /**
     * 操作人
     * 新增的时候为新增人和最后修改人
     * 修改的时候为最后修改人
     */
    @NotNull(message = "操作人不能为空")
    @ApiModelProperty("操作人")
    private Long operator;

}
