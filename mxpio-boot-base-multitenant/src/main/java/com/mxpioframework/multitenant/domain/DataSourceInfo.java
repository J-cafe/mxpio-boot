package com.mxpioframework.multitenant.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Schema(description="数据源")
@Table(name = "MB_DATA_SOURCE_INFO")
public class DataSourceInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_")
	private String id;
	
	@Column(name = "NAME_")
	@Schema(description="数据源名称")
	private String name;
	
	@Column(name = "URL_")
	@Schema(description="数据源URL")
	private String url;
	
	@Column(name = "USERNAME_")
	@Schema(description="数据源用户名")
	private String username;
	
	@Column(name = "PASSWORD_")
	@Schema(description="数据源密码")
	private String password;
	
	@Column(name = "TYPE_")
	@Schema(description="类型")
	private String type;
	
	@Column(name = "DRIVER_CLASS_NAME_")
	@Schema(description="数据源驱动")
	private String driverClassName;
	
	@Column(name = "JNDI_NAME_")
	@Schema(description="JNDI名称")
	private String jndiName;
	
	@Column(name = "SHARED_")
	@Schema(description="是否共享")
	private boolean shared;

	@Column(name = "ENABLED_")
	@Schema(description="是否有效")
	private boolean enabled;
	
	@Column(name = "DEPLETION_INDEX_")
	@Schema(description="用量")
	private Integer depletionIndex = 0;

}
