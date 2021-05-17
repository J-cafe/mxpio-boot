package com.mxpio.mxpioboot.security.entity;

import com.mxpio.mxpioboot.jpa.BaseEntity;

public class Post extends BaseEntity implements Actor {

	private static final long serialVersionUID = 1L;

	@Override
	public String getActorId() {
		return null;
	}

}
