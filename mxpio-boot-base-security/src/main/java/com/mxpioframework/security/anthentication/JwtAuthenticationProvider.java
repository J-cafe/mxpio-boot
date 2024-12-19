package com.mxpioframework.security.anthentication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mxpioframework.security.access.policy.PasswordCheckPolicy;
import com.mxpioframework.security.util.ApplicationContextProvider;
import org.springframework.stereotype.Component;

/**
 * 用户角色校验具体实现
 */
@Component("jwtAuthenticationProvider")
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private List<PasswordCheckPolicy> passwordCheckPolicies;

	public JwtAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, List<PasswordCheckPolicy> passwordCheckPolicies) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.passwordCheckPolicies = passwordCheckPolicies;
	}

	/**
	 * 鉴权具体逻辑
	 *
	 * @param authentication 身份凭证
	 * @return 已认证身份认证
	 * @throws AuthenticationException 认证异常
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
		// 转换authentication 认证时会先调用support方法,受支持才会调用,所以能强转
		JwtLoginToken jwtLoginToken = (JwtLoginToken) authentication;
		defaultCheck(userDetails);
		// 用户名密码校验 具体逻辑
		additionalAuthenticationChecks(userDetails, jwtLoginToken);
		// 构造已认证的authentication
		JwtLoginToken authenticatedToken = new JwtLoginToken(userDetails, jwtLoginToken.getCredentials(),
				userDetails.getAuthorities());
		// authenticatedToken.setDetails(jwtLoginToken.getDetails());
		return authenticatedToken;
	}

	/**
	 * 是否支持当前类
	 *
	 * @param authentication 身份凭证
	 * @return 是否支持
	 */
	public boolean supports(Class<?> authentication) {
		return (JwtLoginToken.class.isAssignableFrom(authentication));
	}

	/**
	 * 一些默认信息的检查
	 *
	 * @param user 用户对象
	 */
	private void defaultCheck(UserDetails user) {
		if (!user.isAccountNonLocked()) {
			throw new LockedException("User account is locked");
		}

		if (!user.isEnabled()) {
			throw new DisabledException("User is disabled");
		}

		if (!user.isAccountNonExpired()) {
			throw new AccountExpiredException("User account has expired");
		}
	}

	/**
	 * 检查密码是否正确
	 *
	 * @param userDetails 用户对象
	 * @param authentication 身份凭证
	 * @throws AuthenticationException 认证异常
	 */
	private void additionalAuthenticationChecks(UserDetails userDetails, JwtLoginToken authentication)
			throws AuthenticationException {
		String passwordCheckPolicyName = ApplicationContextProvider.getEnvironment().getProperty("mxpio.passwordCheckPolicy");
		boolean result = false;
		for(PasswordCheckPolicy passwordCheckPolicy : passwordCheckPolicies){
			if(passwordCheckPolicy.support(passwordCheckPolicyName)){
				result = passwordCheckPolicy.apply(userDetails, authentication, passwordEncoder);
				break;
			}
		}
		if(!result){
			throw new BadCredentialsException("Bad credentials");
		}
		
		/*if (authentication.getCredentials() == null) {
			throw new BadCredentialsException("Bad credentials");
		}
		String presentedPassword = authentication.getCredentials().toString();
		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			throw new BadCredentialsException("Bad credentials");
		}*/
	}

}