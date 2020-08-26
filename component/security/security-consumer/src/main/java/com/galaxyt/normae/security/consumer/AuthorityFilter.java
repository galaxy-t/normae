package com.galaxyt.normae.security.consumer;

import com.galaxyt.normae.core.util.json.GsonUtil;
import com.galaxyt.normae.core.util.seurity.JWTUtil;
import com.galaxyt.normae.security.core.AuthorityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)  //让其尽可能早的被执行
@Component
public class AuthorityFilter implements GlobalFilter {

    @Value("${jwt.key}")
    private String jwtKey;

    @Value("${jwt.sign}")
    private String jwtSign;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //找到必要的参数
        String methodType = exchange.getRequest().getMethodValue();
        Route route = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute");
        String clientId = route.getId();
        String url = exchange.getRequest().getPath().toString();

        //从缓存中拿到当前请求所对应的权限对象
        AuthorityWrapper authority = AuthorityRegistry.INSTANCE.get(clientId, methodType, url);
        //若权限对象不存在则直接返回相应
        if (authority == null) {
            //其它情况全部判定为未授权
            return this.response(exchange, HttpStatus.BAD_REQUEST);
        }

        //若该接口不需要登录 则不再需要解密 token , 直接跳过过滤器即可
        if (!authority.isLogin()) {
            return chain.filter(exchange);
        }

        //其它情况均需要解密 token
        //得到前端通过 header 传递过来的 token
        Optional<String> tokenO = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(this.jwtKey));
        //若 token 存在
        if (tokenO.isPresent()) {
            JWTUtil.JWT jwt = JWTUtil.INSTANCE.check(tokenO.get(), this.jwtSign);
            if (jwt.getStatus() == 0) {     //TOKEN 验证成功

                //若该接口需要对应的权限则验证其权限
                if (authority.isHaveAuthority()) {


                    //若其没有拥有角色或权限则直接响应 401
                    String[] userRoleArr = authority.getRole().split(",");
                    String[] userAuthorityArr = authority.getAuthority().split(",");

                    boolean isHaveAuthority = false;

                    //首先判断其是否拥有该接口所需要的角色
                    for (String userRole : userRoleArr) {
                        if (jwt.getRole().contains(userRole)) {
                            isHaveAuthority = true;
                        }
                    }
                    //若其没有拥有该接口所需要的角色那么再判断是否拥有该接口所需要的权限
                    if (isHaveAuthority == false) {
                        for (String userAuthority : userAuthorityArr) {
                            if (jwt.getAuthority().contains(userAuthority)) {
                                isHaveAuthority = true;
                            }
                        }
                    }

                    //若用户未拥有该接口所需要的任何一个角色或权限则直接返回
                    if (!isHaveAuthority) {
                        return this.response(exchange, HttpStatus.UNAUTHORIZED);
                    }


                }

                //此时认为已经拥有合法权限了 , 也能够正常获取到用户信息
                //此时重新装载 header
                exchange.getRequest().mutate().headers(httpHeaders -> {
                    //删除令牌
                    httpHeaders.remove(this.jwtKey);
                    //将 userId 和 username 放入 header 中 , 传递到下级服务
                    httpHeaders.add(JWTUtil.USER_ID, jwt.getUserId());
                    httpHeaders.add(JWTUtil.USERNAME, jwt.getUsername());
                    httpHeaders.add(JWTUtil.ROLE, GsonUtil.getJson(jwt.getRole()));
                    httpHeaders.add(JWTUtil.AUTHORITY, GsonUtil.getJson(jwt.getAuthority()));

                }).build();

                return chain.filter(exchange);
            }
        }

        return this.response(exchange, HttpStatus.UNAUTHORIZED);

    }


    /**
     * 响应结果
     *
     * @param exchange
     * @param status
     * @return
     */
    public Mono<Void> response(ServerWebExchange exchange, HttpStatus status) {
        //其它情况全部判定为未授权
        ServerHttpResponse response = exchange.getResponse();
        //response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.setStatusCode(status);
        return response.setComplete();
    }
}
