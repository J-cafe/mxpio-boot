package com.mxpioframework.multitenant.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Schema(description="租户")
@Table(name = "MB_ORGANIZATION")
public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_")
	private String id;

	@Column(name = "NAME_")
	@Schema(description="租户名称")
	private String name;

	@Column(name = "DATA_SOURCE_INFO_ID_")
	@Schema(description="数据源ID")
	private String dataSourceInfoId;

	@Transient
	@Schema(description="数据源信息")
	private DataSourceInfo dataSourceInfo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataSourceInfoId() {
		return dataSourceInfoId;
	}

	public void setDataSourceInfoId(String dataSourceInfoId) {
		this.dataSourceInfoId = dataSourceInfoId;
	}

	public DataSourceInfo getDataSourceInfo() {
		return dataSourceInfo;
	}

	public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
		this.dataSourceInfo = dataSourceInfo;
	}


}
