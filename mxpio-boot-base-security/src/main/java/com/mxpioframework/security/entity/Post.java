package com.mxpioframework.security.entity;

import com.mxpioframework.jpa.BaseEntity;

import io.swagger.annotations.ApiModel;

@ApiModel(value="岗位对象")
public class Post extends BaseEntity implements Actor {

	private static final long serialVersionUID = 1L;

	@Override
	public String getActorId() {
		return null;
	}

}
