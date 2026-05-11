package com.mxpioframework.security.access.policy;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mxpioframework.security.anthentication.JwtLoginToken;

public interface PasswordCheckPolicy {

	public boolean support(String passwordCheckPolicyName);
	
	public boolean apply(UserDetails userDetails, JwtLoginToken authentication, PasswordEncoder passwordEncoder);
	
	public String name();
}
