package com.mxpioframework.security;

public class Constants {
/*
	public static final long DEFAULT_ACCESS_TOKEN_TIME_MS = 30 * 60 * 1000L;
	
	public static final long DEFAULT_REFRESH_TOKEN_TIME_MS = 4 * 30 * 60 * 1000L;*/
	
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
            "/v3/api-docs/*",
            "/webjars/**",
            "/doc.html"
    };

	public static final String[] MULTITENANT_WHITELIST = {
			"/multitenant/register/*"
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
		DIGANDLETTERCASE("^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*)", "包含数字、大小写字母"),
		DIGANDLETTER("^(?=.*[0-9])(?=.*[a-zA-Z]).+$", "包含数字和字母"),
		CONTINUOUS("(?!.*(?<re1>[a-zA-Z0-9])\\k<re1>{3})(?<!.*(?<re2>[a-zA-Z0-9])\\k<re2>{3})", "不能是连续的数字、字母"),
		MINLENGTH12("12", "最短长度12"),
		MINLENGTH8("8", "最短长度8"),
		MINLENGTH6("6", "最短长度6");
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

	public enum ThirdAccessTokenKeyEnum {
		DingTalk("-dingtalk-access-token-", "钉钉"), WeChat("-wechat-access-token-", "微信"), WeChatCp("-wechatcp-access-token-", "企业微信");

		ThirdAccessTokenKeyEnum(String code, String name) {
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
	public enum ThirdPlatformTypeEnum {
		DingTalk("dingtalk", "钉钉"),WeChat("wechat", "微信"),WeChatCp("wechatcp", "企业微信"), OAuth("oauth", "统一认证中台"), MSAL("MSAL", "Microsoft Graph身份验证库");

		ThirdPlatformTypeEnum(String code, String name) {
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
