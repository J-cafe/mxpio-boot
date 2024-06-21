package com.mxpioframework.multitenant.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "MB_ORGANIZATION")
public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_")
	private String id;

	@Column(name = "NAME_")
	private String name;

	@Column(name = "DATA_SOURCE_INFO_ID_")
	private String dataSourceInfoId;

	@Transient
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
