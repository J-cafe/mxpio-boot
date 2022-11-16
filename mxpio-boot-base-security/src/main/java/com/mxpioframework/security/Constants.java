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
    
    public enum DatascopeEnum {
		DEPT("DEPT", "部门过滤"), USER("USER", "用户过滤"),DEPT_AND_CHILD("DEPT_AND_CHILD", "部门及子部门过滤"),SERVICE("SERVICE", "服务");

    	DatascopeEnum(String code, String name) {
			this.code = code;
			this.name = name;
		}

		private String code;

		private String name;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public enum RegexEnum {//密码校验 正则表达式
		DIGANDLETTER("^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*)", "包含数字、大小写字母"),
		CONTINUOUS("(?!.*(?<re1>[a-zA-Z0-9])\\k<re1>{3})(?<!.*(?<re2>[a-zA-Z0-9])\\k<re2>{3})", "不能是连续的数字、字母"),
		MINLENGTH("12", "最短长度");

		RegexEnum(String code, String name) {
			this.code = code;
			this.name = name;
		}

		private String code;

		private String name;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
