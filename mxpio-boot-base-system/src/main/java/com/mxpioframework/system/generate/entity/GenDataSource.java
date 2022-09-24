package com.mxpioframework.system.generate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_GENERATE_DATASOURCE")
@Schema(description="数据源")
public class GenDataSource extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_")
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "DS_NAME_")
	@Schema(description = "数据源名称")
	private String dsName;
	
	@Column(name = "DS_TYPE_")
	@Schema(description = "数据源类型")
	private String dsType;
	
	@Column(name = "DS_URL_")
	@Schema(description = "数据源URL")
	private String dsUrl;
	
	@Column(name = "DS_CLASS_")
	@Schema(description = "数据源驱动")
	private String dsClass;
	
	@Column(name = "DS_USER_")
	@Schema(description = "用户名")
	private String dsUser;
	
	@Column(name = "DS_PASS_")
	@Schema(description = "密码")
	private String dsPass;
	
	@Column(name = "MEMO_")
	@Schema(description = "备注")
	private String memo;

}
