package com.mxpioframework.security.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="岗位对象")
public class Post extends BaseEntity implements Actor {

	private static final long serialVersionUID = 1L;

	@Override
	public String getActorId() {
		return null;
	}

}
