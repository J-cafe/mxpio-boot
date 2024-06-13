package com.mxpioframework.dbconsole.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_DB_DBINFO")
@Schema(description="数据源信息")
public class DbInfo extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Generator
	@Schema(description = "ID")
	@Column(name = "ID_")
	private String id;

	@Schema(description = "名称")
	@Column(name = "NAME_")
	private String name;

	@Schema(description = "URL")
	@Column(name = "URL_")
	private String url;

	@Schema(description = "用户名")
	@Column(name = "USERNAME_")
	private String username;

	@Schema(description = "密码")
	@Column(name = "PASSWORD_")
	private String password;

	@Schema(description = "数据库类型")
	@Column(name = "DB_TYPE_")
	private String dbType;

	@Schema(description = "jdbc驱动")
	@Column(name = "DRIVER_CLASS_")
	private String driverClass;

	@Schema(description = "发行名称")
	@Column(name = "PRODUCT_NAME_")
	private String productName;

	@Schema(description = "发行版本")
	@Column(name = "PRODUCT_VERSION_")
	private String productVersion;

}
