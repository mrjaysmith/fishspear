package com.fisher.fishspear.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fisher.fishspear.common.shiro.ShiroUser;
import com.fisher.fishspear.entity.SysAdmin;

import java.util.Date;

/**
 * @author Mr.Li
 * @create 2018-07-12 14:23
 * @desc JWT工具类
 **/
public class JwtUtil {

    /**
     * token存活时间(小时)
     */
    private static final int TTL = 24;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static void verify(String token, String username, String secret) throws JWTVerificationException{
        //根据密码生成JWT效验器
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("username", username)
                .build();
        //效验TOKEN
        verifier.verify(token);
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户id
     */
    public static Integer getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asInt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的clientType
     */
    public static String getClientType(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("client").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名
     * @param user
     * @return 加密的token
     */
    public static String sign(SysAdmin user) {
        Date date = new Date(System.currentTimeMillis() + TTL * 60 * 60 * 1000);
        Algorithm algorithm = Algorithm.HMAC256(user.getPassword());
        // 附带username信息
        return JWT.create()
                .withClaim("username", user.getUsername())
                .withClaim("userId", user.getId())
                .withExpiresAt(date)
                .sign(algorithm);

    }
}
