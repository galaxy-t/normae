package com.galaxyt.normae.uaa.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.galaxyt.normae.uaa.enums.Disabled;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限列表 Vo
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/13 15:55
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/13 15:55     jiangxd          v1.0.0           Created
 */
@Data
@ApiModel(description = "权限列表Vo")
public class AuthorityVo {

    /**
     * 唯一标识
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("权限id")
    private Long id;

    /**
     * 所属项目
     */
    @ApiModelProperty("所属项目")
    private String app;

    /**
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    private String name;

    /**
     * 权限标识
     */
    @ApiModelProperty("权限标识")
    private String mark;

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
