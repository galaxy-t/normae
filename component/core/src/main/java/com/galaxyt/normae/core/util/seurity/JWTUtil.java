package com.galaxyt.normae.core.util.seurity;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JWT工具类
 * 单利模式
 * 使用HS512加密算法
 * 单例
 */
public enum JWTUtil {


    INSTANCE;

    /**
     * 存放在 body 中的 userId 的 key
     */
    public static final String USER_ID = "userId";

    /**
     * 存放在 body 中的 username 的 key
     */
    public static final String USERNAME = "username";

    /**
     * 存放在 body 中的 角色集合 的 key
     */
    private static final String ROLES = "roles";

    /**
     * 存放在 body 中的 权限集合 的 key
     */
    private static final String AUTHORITIES = "authorities";


    public String generate(String userId, String username, List<String> roles, List<String> authorities,
                           String subject, String sign, Long exp) {

        Date now = new Date();
        Date expDate = new Date(now.getTime() + exp);

        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID, userId);
        claims.put(USERNAME, username);
        claims.put(ROLES, roles);
        claims.put(AUTHORITIES, authorities);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, sign)
                .compact();
    }

    /**
     * 生成JWT TOKEN
     *
     * @param claims 需要保存到 TOKEN 中的信息
     * @param sign   签名密钥，加密算法需要，对称加密
     * @param exp    过期时间，毫秒值
     * @return 生成的JWT TOKEN
     */
    /*public String generate(Map<String, Object> claims, String subject, String sign, Long exp) {

        Date now = new Date();
        Date expDate = new Date(now.getTime() + exp);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, sign)
                .compact();
    }*/


    /**
     * 验证JWT TOKEN
     * @param token     要验证的TOKEN
     * @param sign    签名密钥，解密需要，对称解密
     * @return
     *      TOKEN验证成功返回JWTResult对象
     * @throws ExpiredJwtException  TOKEN过期抛错
     * @throws SignatureException   签名验证失败抛错
     */
    public JWT check(String token, String sign) throws ExpiredJwtException, SignatureException {

        try {
            // parse the token.
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(sign)
                    .parseClaimsJws(token)
                    .getBody();

            return new JWT(0, body);
        } catch (ExpiredJwtException e) {       //若TOKEN过期则直接抛错
            return new JWT(1, null);
        } catch (SignatureException e) {        //若签名验证失败则直接抛错
            return new JWT(2, null);
        }catch (Exception e) {
            return new JWT(3, null);
        }

    }

    /**
     * TOKEN 解析结果
     */
    public class JWT {

        /**
         * 结果解析状态
         *         0：正常
         *         1：TOKEN 已过期
         *         2：签名验证失败
         *         3：未知其它异常
         */
        private int status;

        /**
         * 当且仅当 status = 0 时候，该属性有意义
         */
        private Map<String, Object> body;

        public JWT(int status, Map<String, Object> body) {
            this.status = status;
            this.body = body;
        }

        public int getStatus() {
            return status;
        }

        public String getUserId() {
            return String.valueOf(this.body.get(USER_ID));
        }

        public String getUsername() {
            return String.valueOf(this.body.get(USERNAME));
        }

        public List<String> getRoles() {
            return (List<String>) this.body.get(ROLES);
        }

        public List<String> getAuthorities() {
            return (List<String>) this.body.get(AUTHORITIES);
        }



        @Override
        public String toString() {
            return "JWT{" +
                    "status=" + status +
                    ", body=" + body +
                    '}';
        }
    }


}
