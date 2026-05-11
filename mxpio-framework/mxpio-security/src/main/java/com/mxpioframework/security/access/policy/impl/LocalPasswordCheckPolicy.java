package com.mxpioframework.security.access.policy.impl;

import com.mxpioframework.security.util.AesEncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mxpioframework.security.access.policy.PasswordCheckPolicy;
import com.mxpioframework.security.anthentication.JwtLoginToken;

@Component
public class LocalPasswordCheckPolicy implements PasswordCheckPolicy {
	@Value("${mxpio.JSEncrypt.privateKey:}")
	private String privateKey;
	
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
		if (StringUtils.isNotBlank(privateKey)&&!StringUtils.equals("null",privateKey)) {
            try {
                presentedPassword = AesEncryptUtil.decrypt(presentedPassword, privateKey);
            } catch (Exception e) {
                throw new RuntimeException(e+"密码解密失败");
            }
        }
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
