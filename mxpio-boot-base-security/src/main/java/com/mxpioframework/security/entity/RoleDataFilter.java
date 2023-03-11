package com.mxpioframework.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_ROLE_DATA_FILTER")
@Schema(description="角色关联数据过滤")
@ToString
public class RoleDataFilter extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_", length = 64)
	@Generator
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "ROLE_ID_", length = 64)
	@Schema(description = "角色ID")
	private String roleId;
	
	@Column(name = "DATA_FILTER_ID_", length = 64)
	@Schema(description = "数据过滤ID")
	private String dataFilterId;
	
}
