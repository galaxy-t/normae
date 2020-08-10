package com.galaxyt.normae.uaa.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.galaxyt.normae.uaa.enums.Disabled;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色 返回
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/9 9:04
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/9 9:04     jiangxd          v1.0.0           Created
 */
@Data
@ApiModel(description = "角色")
public class RoleVo implements Serializable {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

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
     * 角色标识
     */
    @ApiModelProperty("角色标识")
    private String mark;

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

    /**
     * 创建者主键id
     */
    @ApiModelProperty("创建者主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改者主键id
     */
    @ApiModelProperty("修改者主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
