package com.mxpio.mxpioboot.security.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.access.ConfigAttribute;

import com.mxpio.mxpioboot.jpa.BaseEntity;
import com.mxpio.mxpioboot.jpa.annotation.Generator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "MB_ELEMENT")
public class Element extends BaseEntity implements Resource {

	private static final long serialVersionUID = 1L;

	public static final String RESOURCE_TYPE = "ELEMENT";

	@Id
	@Generator
	@Column(name = "ID_", length = 64)
	private String id;

	@Column(name = "ELEMENT_ID_", length = 255)
	private String elementId;

	@Column(name = "ELEMENT_TYPE", length = 64)
	@Enumerated(EnumType.STRING)
	private ElementType elementType;

	@Column(name = "NAME_", length = 64)
	private String name;

	@Column(name = "URL_ID_", length = 64)
	private String urlId;

	@Column(name = "PATH_", length = 512)
	private String path;

	@Column(name = "DESCRIPTION_", length = 512)
	private String description;

	@Transient
	private boolean authorized;

	@Transient
	private String configAttributeId;

	@Transient
	private List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

}
