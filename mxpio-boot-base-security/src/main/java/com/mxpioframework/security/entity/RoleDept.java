package com.mxpioframework.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_ROLE_DEPT")
@ApiModel(value="部门角色关系")
public class RoleDept extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	@ApiModelProperty(value = "ID", hidden = true)
	private String id;

	@ApiModelProperty(value = "角色ID")
	@Column(name = "ROLE_ID_")
	private String roleId;
	
	@ApiModelProperty(value = "部门ID")
	@Column(name = "DEPT_ID_")
	private String deptId;
	
}
