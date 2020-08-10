package com.galaxyt.normae.uaa.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxyt.normae.security.core.Authority;
import com.galaxyt.normae.uaa.pojo.dto.AuthorityDto;
import com.galaxyt.normae.uaa.pojo.vo.AuthorityVo;
import com.galaxyt.normae.uaa.service.AuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 权限
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/7/13 15:07
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/13 15:07     zhouqi          v1.0.0           Created
 */
@RestController
@RequestMapping("/authority")
@Api(tags = "权限管理")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;


    /**
     * 新增权限
     *
     * @param authorityDto 权限新增参数
     */
    @Authority(mark = "authadd", name = "新增权限")
    @PutMapping
    @ApiOperation(value = "权限新增")
    public void add(@ApiParam("权限新增参数") @RequestBody @Valid AuthorityDto authorityDto) {
        this.authorityService.add(authorityDto);
    }

    /**
     * 修改权限
     *
     * @param authorityId  权限id
     * @param authorityDto 权限修改参数
     */
    @Authority(mark = "authedit", name = "权限修改")
    @PatchMapping
    @ApiOperation(value = "权限修改")
    public void edit(@ApiParam("权限id") @NotNull(message = "权限id不能为空") @RequestParam("authorityId") Long authorityId,
                     @ApiParam("权限修改参数") @RequestBody @Valid AuthorityDto authorityDto) {
        this.authorityService.edit(authorityId, authorityDto);
    }

    /**
     * 列表
     * 分页查看
     */
    @Authority(mark = "authlist", name = "权限列表")
    @GetMapping
    @ApiOperation(value = "权限列表")
    public Page<AuthorityVo> list(@ApiParam("每页条数") @NotNull(message = "每页条数不能为空") @RequestParam("pageSize") Integer pageSize,
                                  @ApiParam("页数") @NotNull(message = "页数不能为空") @RequestParam("pageIndex") Integer pageIndex,
                                  @ApiParam("所属项目") @RequestParam("app") String app) {
        Page<AuthorityVo> page = new Page<>(pageIndex, pageSize);
        this.authorityService.list(page, app);
        return page;
    }

    /**
     * 删除
     * 仅提供删除一条
     *
     * @param authorityId 权限id
     */
    @Authority(mark = "authdel", name = "权限删除")
    @DeleteMapping
    @ApiOperation(value = "权限删除")
    public void remove(@ApiParam("权限id") @NotNull(message = "权限id不能为空") @RequestParam Long authorityId) {
        this.authorityService.remove(authorityId);
    }

    /**
     * 权限启用禁用
     * 若禁用则会启用
     * 若启用则会禁用
     *
     * @param authorityId  权限主键 ID
     */
    @Authority(mark = "disabled", name = "权限启用禁用")
    @PatchMapping("disabled")
    @ApiOperation(value = "权限启用禁用")
    public void disabled(@ApiParam("权限启用禁用") @NotNull(message = "权限id不能为空") @RequestParam Long authorityId) {
        this.authorityService.disabled(authorityId);
    }

}
