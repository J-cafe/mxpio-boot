package com.mxpioframework.security.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "MB_DEPT")
@ApiModel(value="部门")
public class Dept extends BaseEntity implements Actor {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@ApiModelProperty(value = "ID")
	@Column(name = "ID_")
	private String id;

	@ApiModelProperty(value = "部门类型")
	@Column(name = "DEPT_TYPE_")
	@com.mxpioframework.security.annotation.Dict(dicCode = "MB_SYSTEM_DEPT_TYPE")
	private Integer deptType;
	
	@ApiModelProperty(value = "部门代码")
	@Column(name = "DEPT_CODE_", length=20, nullable=false, unique=true)
	private String deptCode;
	
	@ApiModelProperty(value = "部门名称")
	@Column(name = "DEPT_NAME_", length=20, nullable=false)
	private String deptName;
	
	@ApiModelProperty(value = "部门描述")
	@Column(name = "DEPT_DESC_")
	private String deptDesc;
	
	@ApiModelProperty(value = "父部门ID")
	@Column(name = "FA_DEPT_ID_")
	@com.mxpioframework.security.annotation.Dict(dicCode = "deptCode", dicEntity= Dept.class, dicText= "deptName")
	private String faDeptId;
	
	@Transient
	private List<Dept> children;

	public Integer getDeptType() {
		return deptType;
	}

	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

	public List<Dept> getChildren() {
		return children;
	}

	public void setChildren(List<Dept> children) {
		this.children = children;
	}

	public String getFaDeptId() {
		return faDeptId;
	}

	public void setFaDeptId(String faDeptId) {
		this.faDeptId = faDeptId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getActorId() {
		return id;
	}
	
}
