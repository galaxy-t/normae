package com.galaxyt.normae.uaa.web;

import com.galaxyt.normae.api.uaa.dto.UaaUserDto;
import com.galaxyt.normae.api.uaa.vo.UserRoleVo;
import com.galaxyt.normae.core.annotation.NotWrapper;
import com.galaxyt.normae.security.core.Authority;
import com.galaxyt.normae.uaa.pojo.dto.UserRoleDto;
import com.galaxyt.normae.uaa.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * uaa 用户 Controller
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/10 13:24
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/10 13:24     jiangxd          v1.0.0           Created
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 用户新增
     * id不能为空，业务系统新增用户成功后将id传到uaa
     * 需要调用方自行验证帐号密码的合法性
     * feign调用
     *
     * @param uaaUserDto
     * @return GlobalExceptionCode code
     */
    @NotWrapper
    @PutMapping
    public int add(@RequestBody UaaUserDto uaaUserDto) {
        return this.userService.add(uaaUserDto);
    }

    /**
     * 修改密码
     * feign调用
     *
     * @param uaaUserDto 修改密码参数
     */
    @NotWrapper
    @PatchMapping("password")
    public boolean password(@RequestBody UaaUserDto uaaUserDto) {
        return this.userService.password(uaaUserDto);
    }

    /**
     * 删除用户及用户角色关系
     * feign调用
     *
     * @param userId 用户id
     */
    @NotWrapper
    @DeleteMapping
    public boolean remove(@RequestParam("userId") Long userId) {
        return this.userService.remove(userId);
    }

    /**
     * 修改用户角色
     *
     * @param userRoleDto
     */
    @Authority(mark = "userattr,userrole", name = "用户角色")
    @PatchMapping("role")
    @ApiOperation(value = "修改用户角色")
    public void role(@RequestBody @Valid UserRoleDto userRoleDto) {
        this.userService.role(userRoleDto);
    }

    /**
     * 根据用户id查看用户角色id和角色名称
     * 用于业务系统用户列表的角色展示
     * feign调用
     *
     * @param userIds 用户ids
     */
    @NotWrapper
    @PostMapping("role")
    public List<UserRoleVo> userRoleList(@RequestBody List<Long> userIds) {
        return this.userService.userRoleList(userIds);
    }

}
