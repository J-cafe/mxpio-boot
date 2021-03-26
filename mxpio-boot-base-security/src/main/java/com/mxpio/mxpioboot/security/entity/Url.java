package com.mxpio.mxpioboot.security.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.access.ConfigAttribute;

import com.mxpio.mxpioboot.jpa.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_URL")
public class Url extends BaseEntity implements Resource {

	private static final long serialVersionUID = 1L;

	/**

	 * 菜单资源类型

	 */
	public static final String RESOURCE_TYPE = "URL";
	
	@Id
	@Column(name = "ID_", length = 64)
	private String id;
	
	@Column(name = "NAME_", length = 64)
	private String name;
	
	@Column(name = "ICON_", length = 255)
	private String icon;
	
	@Column(name = "PATH_", length = 512)
	private String path;
	
	@Column(name = "PARENT_ID_", length = 64)
	private String parentId;
	
	@Column(name = "ORDER_")
	private Integer order;
	
	@Column(name = "NAVIGABLE_")
	private boolean navigable;
	
	@Column(name = "DESCRIPTION_", length = 255)
	private String description;
	
	@Transient
	private List<Url> children;
	
	@Transient
	private List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

}
