package com.mxpioframework.security;

public class Constants {

	public static final long DEFAULT_ACCESS_TOKEN_TIME_MS = 30 * 60 * 1000L;
	
	public static final long DEFAULT_REFRESH_TOKEN_TIME_MS = 4 * 30 * 60 * 1000L;
	
	public static final String JWT_TOKEN_SALT = "MXPIO";
	
	public static final String JWT_ACCESS_TOKEN_REDIS_KEY = "-jwt-token-";
	
	public static final String JWT_REFRESH_TOKEN_REDIS_KEY = "-jwt-token-";
	
	public static final String CAPTCHA_REDIS_KEY = "-captcha-";
	
	// Swagger WHITELIST
    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/webjars/**",
            "/doc.html"
    };

}
