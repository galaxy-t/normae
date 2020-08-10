package com.galaxyt.normae.api.uaa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色信息
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/15 10:55
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/15 10:55     jiangxd          v1.0.0           Created
 */
@Data
public class RoleDetail {

    /**
     * 所属项目
     */
    private String app;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色标识
     */
    private String mark;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 是否启用
     */
    private int disabled;

    /**
     * 权限ids
     */
    private List<Long> authorityIds;
}
