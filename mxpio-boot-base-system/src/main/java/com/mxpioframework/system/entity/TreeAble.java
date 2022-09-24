package com.mxpioframework.system.entity;

import java.util.List;

import com.mxpioframework.security.entity.BaseEntity;

public interface TreeAble<T extends BaseEntity> {

	public List<T> getTreeChildren();
	
	public String getCurrentNodeKey();
	
	public String getParentNodeKey();
	
	public void setTreeChildren(List<T> children);
}
