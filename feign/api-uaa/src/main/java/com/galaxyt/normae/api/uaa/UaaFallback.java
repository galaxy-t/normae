package com.galaxyt.normae.api.uaa;

import com.galaxyt.normae.api.uaa.dto.UaaUserDto;
import com.galaxyt.normae.api.uaa.vo.RoleDetail;
import com.galaxyt.normae.api.uaa.vo.UserDetail;
import com.galaxyt.normae.api.uaa.vo.UserRoleVo;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UAA 服务熔断
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
@Slf4j
@Component
public class UaaFallback implements FallbackFactory<UaaClient> {

    @Override
    public UaaClient create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new UaaClient() {

            @Override
            public int add(UaaUserDto uaaUserDto) {
                log.error("用户[{}]注册失败", uaaUserDto.getUsername());
                return GlobalExceptionCode.FEIGN_ERROR.getCode();
            }

            @Override
            public UserDetail authToken(String app, String username, String password) {
                log.error("项目[{}],用户[{}]授权失败", app, username);
                return null;
            }

            @Override
            public boolean remove(Long userId) {
                log.error("用户[{}]删除失败", userId);
                return Boolean.FALSE;
            }

            @Override
            public boolean password(UaaUserDto uaaUserDto) {
                log.error("用户[{}]密码修改失败", uaaUserDto.getId());
                return Boolean.FALSE;
            }

            @Override
            public List<UserRoleVo> userRoleList(List<Long> userIds) {
                log.error("用户[{}]角色查询失败", userIds);
                return null;
            }

            @Override
            public RoleDetail roleDetail(Long roleId) {
                log.error("角色[{}]查询失败", roleId);
                return null;
            }

        };
    }


}
