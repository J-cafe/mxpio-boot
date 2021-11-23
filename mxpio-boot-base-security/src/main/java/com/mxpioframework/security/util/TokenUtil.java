package com.mxpioframework.security.util;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mxpioframework.security.Constants;
import com.mxpioframework.security.entity.User;

public class TokenUtil {
	
	public static String createAccessToken(User jwtUserDetails) {
		String json = JSON.toJSONString(jwtUserDetails);
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + Constants.DEFAULT_ACCESS_TOKEN_TIME_MS;
        Date exp = new Date(expMillis);
        Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_TOKEN_SALT);
        String token = JWT.create()
    		.withSubject(json)
            .withExpiresAt(exp)
            .withIssuedAt(now)
            .sign(algorithm);
		return token;
	}
	
	public static String createRefreshToken(String accessToken) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + Constants.DEFAULT_REFRESH_TOKEN_TIME_MS;
        Date exp = new Date(expMillis);
        Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_TOKEN_SALT);
        String token = JWT.create()
    		.withSubject(accessToken)
            .withExpiresAt(exp)
            .withIssuedAt(now)
            .sign(algorithm);
		return token;
	}
	
	public static DecodedJWT verifyToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_TOKEN_SALT);
        JWTVerifier v = JWT.require(algorithm).build();
        DecodedJWT jwt = v.verify(token);
        return jwt;
	}
	
	public static String verifySubject(String token) {
		DecodedJWT jwt = verifyToken(token);
        return jwt.getSubject();
	}
	
	public static User getUser(String token) {
        User user = JSON.parseObject(verifySubject(token), User.class);
        return user;
	}

}
