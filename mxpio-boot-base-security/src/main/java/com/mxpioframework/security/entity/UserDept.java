package com.mxpioframework.security.entity;

import javax.persistence.*;

import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "MB_USER_DEPT")
@Schema(description="用户部门关系")
public class UserDept extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Schema(description = "ID")
	@Column(name = "ID_")
	private String id;

	@Schema(description = "USER_ID")
	@Column(name = "USER_ID_")
	private String userId;
	
	@Schema(description = "DEPT_ID")
	@Column(name = "DEPT_ID_")
	private String deptId;

	@Transient
	@Schema(description = "部门")
	private Dept dept;

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

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}
}
