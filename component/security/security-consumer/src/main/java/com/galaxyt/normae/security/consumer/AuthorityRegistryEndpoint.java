package com.galaxyt.normae.security.consumer;

import com.galaxyt.normae.core.annotation.NotWrapper;
import com.galaxyt.normae.security.core.AuthorityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 接收微服务推送的权限信息并缓存
 *
 * @author jiangxd
 * @version v1.0.0
 * @date 2020/7/3 13:11
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/3 13:11     jiangxd          v1.0.0           Created
 */
@Slf4j
@RestController
@RequestMapping("/endpoint/authority/registry")
public class AuthorityRegistryEndpoint {

    /**
     * 接收微服务推送的权限信息并缓存
     *
     * @param appId            服务id
     * @param authoritys       权限信息
     * @return true成功|false失败
     */
    @NotWrapper
    @PostMapping
    public Boolean notifications(@RequestParam("appId") String appId,
                                 @RequestBody AuthorityWrapper[] authoritys) {
        AuthorityRegistry.INSTANCE.put(appId, authoritys);
        return Boolean.TRUE;
    }

}
