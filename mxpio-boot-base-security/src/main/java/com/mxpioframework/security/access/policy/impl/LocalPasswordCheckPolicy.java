package com.mxpioframework.security.access.policy.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mxpioframework.security.access.policy.PasswordCheckPolicy;
import com.mxpioframework.security.anthentication.JwtLoginToken;

@Component
public class LocalPasswordCheckPolicy implements PasswordCheckPolicy {
	
	@Override
	public boolean support(String passwordCheckPolicyName) {
		return name().equals(passwordCheckPolicyName);
	}

	@Override
	public boolean apply(UserDetails userDetails, JwtLoginToken authentication, PasswordEncoder passwordEncoder) {
		if (authentication.getCredentials() == null) {
			return false;
		}
		String presentedPassword = authentication.getCredentials().toString();
		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			return false;
		}
		return true;
	}

	@Override
	public String name() {
		return "LocalPasswordCheckPolicy";
	}

}
