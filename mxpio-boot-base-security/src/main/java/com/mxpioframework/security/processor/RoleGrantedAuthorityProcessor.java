package com.mxpioframework.security.processor;

import com.mxpioframework.security.entity.RoleGrantedAuthority;

public interface RoleGrantedAuthorityProcessor {
	
	boolean preProcess(Context<RoleGrantedAuthority> context);

	void postProcess(Context<RoleGrantedAuthority> context);

}
