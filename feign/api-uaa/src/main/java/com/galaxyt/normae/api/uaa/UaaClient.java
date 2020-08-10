package com.galaxyt.normae.api.uaa;

import com.galaxyt.normae.api.uaa.dto.UaaUserDto;
import com.galaxyt.normae.api.uaa.vo.RoleDetail;
import com.galaxyt.normae.api.uaa.vo.UserDetail;
import com.galaxyt.normae.api.uaa.vo.UserRoleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UAA 服务 Feign 客户端
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/28 14:18
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/28 14:18     zhouqi          v1.0.0           Created
 */
@Primary
@FeignClient(value = "uaa", fallbackFactory = UaaFallback.class)
public interface UaaClient {

    /**
     * 根据用户名和密码获取用户的 JWT 令牌
     *
     * @param app      所属项目
     * @param username 用户名
     * @param password 密码  加密后的 , 请求之后 token 方法不会对该密码进行加密 , 不可为 null , 但允许为 "" , 若为空字符串则代表本次请求为无密码登录 , 可用于短信验证码登录等方式
     */
    @GetMapping("/auth/token")
    UserDetail authToken(@RequestParam("app") String app,
                         @RequestParam("username") String username,
                         @RequestParam(value = "password", required = false) String password);

    /**
     * 用户新增
     * id不能为空，业务系统新增用户成功后将id传到uaa
     * 需要调用方自行验证帐号密码的合法性
     *
     * @param uaaUserDto 用户注册参数
     */
    @PutMapping("/user")
    int add(@RequestBody UaaUserDto uaaUserDto);

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @DeleteMapping("/user")
    boolean remove(@RequestParam("userId") Long userId);

    /**
     * 修改密码
     *
     * @param uaaUserDto 修改密码参数
     */
    @PatchMapping("/user/password")
    boolean password(@RequestBody UaaUserDto uaaUserDto);

    /**
     * 根据用户id查看用户角色id和角色名称
     * 用于业务系统用户列表的角色展示
     *
     * @param userIds
     */
    @PostMapping("/user/role")
    List<UserRoleVo> userRoleList(@RequestBody List<Long> userIds);

    /**
     * 查看角色信息
     *
     * @param roleId 角色id
     */
    @GetMapping("/role")
    RoleDetail roleDetail(@RequestParam("roleId") Long roleId);

}
