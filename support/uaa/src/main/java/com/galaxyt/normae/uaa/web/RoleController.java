package com.galaxyt.normae.uaa.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxyt.normae.api.uaa.vo.RoleDetail;
import com.galaxyt.normae.core.annotation.NotWrapper;
import com.galaxyt.normae.core.thread.CurrentUser;
import com.galaxyt.normae.security.core.Authority;
import com.galaxyt.normae.uaa.pojo.dto.RoleAuthorityDto;
import com.galaxyt.normae.uaa.pojo.dto.RoleDto;
import com.galaxyt.normae.uaa.pojo.dto.RoleQueryDto;
import com.galaxyt.normae.uaa.pojo.vo.RoleVo;
import com.galaxyt.normae.uaa.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 角色管理 Controller
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/9 15:49
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/9 15:49     jiangxd          v1.0.0           Created
 */
@RestController
@RequestMapping("role")
@Api(tags = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;


    /**
     * 角色新增
     *
     * @param roleDto 角色新增参数
     */
    @Authority(mark = "roleattr,roleadd", name = "角色新增")
    @PutMapping
    @ApiOperation(value = "角色新增")
    public Long add(@ApiParam("角色新增参数") @RequestBody RoleDto roleDto) {
        Long currentUserId = CurrentUser.getId();
        return this.roleService.add(roleDto, currentUserId);
    }

    /**
     * 角色修改
     *
     * @param roleId  角色id
     * @param roleDto 角色修改参数
     */
    @Authority(mark = "roleattr,roleedit", name = "角色修改")
    @PatchMapping
    @ApiOperation(value = "角色修改")
    public void edit(@ApiParam("角色id") @NotNull(message = "角色id不能为空") @RequestParam("roleId") Long roleId,
                     @ApiParam("角色修改参数") @RequestBody RoleDto roleDto) {
        Long currentUserId = CurrentUser.getId();
        this.roleService.edit(currentUserId, roleId, roleDto);
    }

    /**
     * 角色删除
     *
     * @param roleId 角色id
     */
    @Authority(mark = "roleattr,roledel", name = "角色删除")
    @DeleteMapping
    @ApiOperation(value = "角色删除")
    public void remove(@ApiParam("角色id") @NotNull(message = "角色id不能为空") @RequestParam("roleId") Long roleId) {
        this.roleService.remove(roleId);
    }

    /**
     * 分页查看角色列表
     * 根据角色名称、角色描述模糊查询、分页查询
     *
     * @param roleQueryDto 角色查询参数
     */
    @Authority(mark = "rolelist", name = "角色列表")
    @PostMapping("list")
    @ApiOperation(value = "角色列表")
    public Page<RoleVo> list(@ApiParam("每页条数") @NotNull(message = "每页条数不能为空") @RequestParam("pageSize") Integer pageSize,
                             @ApiParam("页数") @NotNull(message = "页数不能为空") @RequestParam("pageIndex") Integer pageIndex,
                             @RequestBody RoleQueryDto roleQueryDto) {
        Page<RoleVo> page = new Page<>(pageIndex, pageSize);
        this.roleService.list(page, roleQueryDto);
        return page;
    }

    /**
     * 角色列表
     * 用户选择角色列表
     */
    @Authority(mark = "userattr,rolelist", name = "角色列表")
    @GetMapping("list")
    @ApiOperation(value = "角色列表--用户选择角色列表")
    public Map<String, Object> roleList() {
        return this.roleService.list();
    }

    /**
     * 角色修改权限
     *
     * @param roleAuthorityDto 角色权限
     */
    @Authority(mark = "roleattr,roleauth", name = "角色修改权限")
    @PatchMapping("authority")
    @ApiOperation(value = "角色修改权限")
    public void authority(@RequestBody @Valid RoleAuthorityDto roleAuthorityDto) {
        this.roleService.authority(roleAuthorityDto);
    }

    /**
     * 根据角色id查看角色信息
     * feign调用
     *
     * @param roleId
     */
    @NotWrapper
    @GetMapping
    public RoleDetail detail(@RequestParam("roleId") Long roleId) {
        return this.roleService.detail(roleId);
    }

}
