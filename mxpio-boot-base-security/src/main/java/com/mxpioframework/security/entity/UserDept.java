package com.mxpioframework.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "MB_USER_DEPT")
public class UserDept extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@ApiModelProperty(value = "ID")
	@Column(name = "ID_")
	private String id;

	@ApiModelProperty(value = "USER_ID")
	@Column(name = "USER_ID_")
	private String userId;
	
	@ApiModelProperty(value = "DEPT_ID")
	@Column(name = "DEPT_ID_")
	private String deptId;
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
