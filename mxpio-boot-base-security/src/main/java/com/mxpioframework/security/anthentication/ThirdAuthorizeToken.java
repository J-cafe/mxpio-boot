package com.mxpioframework.security.anthentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 三方用户鉴权：
 */
public class ThirdAuthorizeToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;
	/** 登录用户信息 */
	private final Object principal;
	/** 密码 */
	private Object credentials;//冗余字段，存放用户授权code

	private Object	thirdPlatformType;//三方平台类型
	/**
	 * 创建一个未认证的授权令牌, 这时传入的principal是用户名
	 *
	 */
	public ThirdAuthorizeToken(Object principal,Object authCode,Object thirdPlatformType) {
		super(null);
		this.principal = principal;
		this.credentials = authCode;
		this.thirdPlatformType = thirdPlatformType;
		setAuthenticated(false);
	}

	/**
	 * 创建一个已认证的授权令牌,如注释中说的那样,这个方法应该由AuthenticationProvider来调用
	 * 也就是我们写的JwtAuthenticationProvider,有它完成认证后再调用这个方法,
	 * 这时传入的principal为从userService中查出的UserDetails
	 *
	 * @param principal
	 * @param credentials
	 * @param authorities
	 */
	public ThirdAuthorizeToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true);
		super.setDetails(principal);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	public Object getThirdPlatformType(){return this.thirdPlatformType;}
}
