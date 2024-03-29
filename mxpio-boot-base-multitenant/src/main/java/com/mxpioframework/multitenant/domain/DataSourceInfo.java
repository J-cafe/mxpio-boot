package com.mxpioframework.multitenant.domain;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "MB_DATA_SOURCE_INFO")
public class DataSourceInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_")
	private String id;
	
	@Column(name = "NAME_")
	private String name;
	
	@Column(name = "URL_")
	private String url;
	
	@Column(name = "USERNAME_")
	private String username;
	
	@Column(name = "PASSWORD_")
	private String password;
	
	@Column(name = "TYPE_")
	private String type;
	
	@Column(name = "DRIVER_CLASS_NAME_")
	private String driverClassName;
	
	@Column(name = "JNDI_NAME_")
	private String jndiName;
	
	@Column(name = "SHARED_")
	private boolean shared;

	@Column(name = "ENABLED_")
	private boolean enabled;
	
	@Column(name = "DEPLETION_INDEX_")
	private Integer depletionIndex = 0;

}
