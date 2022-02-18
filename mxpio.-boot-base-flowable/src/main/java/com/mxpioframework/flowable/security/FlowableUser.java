package com.mxpioframework.flowable.security;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.security.entity.User;

public class FlowableUser extends User implements org.flowable.idm.api.User {

	private static final long serialVersionUID = 1L;
	
	public FlowableUser create(User user) {
		FlowableUser fUser = new FlowableUser();
		BeanReflectionUtils.copyProperties(fUser, user);
		return fUser;
	}

	@Override
	public String getId() {
		return super.getUsername();
	}

	@Override
	public void setId(String id) {
		super.setUsername(id);		
	}

	@Override
	public String getFirstName() {
		return "";
	}

	@Override
	public void setFirstName(String firstName) {
		
	}

	@Override
	public void setLastName(String lastName) {
		super.setNickname(lastName);
	}

	@Override
	public String getLastName() {
		return super.getNickname();
	}

	@Override
	public void setDisplayName(String displayName) {
		super.setNickname(displayName);
	}

	@Override
	public String getDisplayName() {
		return super.getNickname();
	}

	@Override
	public String getTenantId() {
		return null;
	}

	@Override
	public void setTenantId(String tenantId) {
		
	}

	@Override
	public boolean isPictureSet() {
		return false;
	}

}
